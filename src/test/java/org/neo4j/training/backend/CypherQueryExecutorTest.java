package org.neo4j.training.backend;

import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.cypher.SyntaxException;
import org.neo4j.graphdb.*;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;
import static org.junit.internal.matchers.IsCollectionContaining.hasItems;

/**
 * @author mh
 * @since 22.04.12
 */
public class CypherQueryExecutorTest {

    private GraphDatabaseService gdb;
    private CypherQueryExecutor cypherQueryExecutor;
    private Node refNode;

    @Before
    public void setUp() throws Exception {
        gdb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        try (Transaction tx = gdb.beginTx()) {
            refNode = gdb.createNode();
            tx.success();
        }
        cypherQueryExecutor = new CypherQueryExecutor(gdb, new Index(gdb));
    }

    @After
    public void tearDown() throws Exception {
        gdb.shutdown();
    }

    @Test
    public void testIsMutatingQuery() throws Exception {
        assertFalse(cypherQueryExecutor.isMutatingQuery(""));
        assertFalse(cypherQueryExecutor.isMutatingQuery("match (n) where id(n) = (1) return n"));
        assertTrue(cypherQueryExecutor.isMutatingQuery("match (n) where id(n) = (1) create (m { name: 'Andres'})"));
        assertTrue(cypherQueryExecutor.isMutatingQuery("match (n) where id(n) = (1) create n-[:KNOWS]->n"));
        assertTrue(cypherQueryExecutor.isMutatingQuery("match (n) where id(n) = (1) delete n"));
        assertTrue(cypherQueryExecutor.isMutatingQuery("match (n) where id(n) = (1) set n.name = 'Andres'"));
    }

    @Test
    public void testExtractProperties() throws Exception {
        assertTrue(cypherQueryExecutor.extractProperties("").isEmpty());
        assertTrue(cypherQueryExecutor.extractProperties("match (n) where id(n) = (1) return n").isEmpty());
        assertThat(cypherQueryExecutor.extractProperties("match (n) where id(n) = (1) create (m { name: 'Andres'})"), hasItem("name"));
        assertThat(cypherQueryExecutor.extractProperties("match (n) where id(n) = (1) create n-[:KNOWS {name:'Friends', since : 2000}]->n"), hasItems("name", "since"));
        assertThat(cypherQueryExecutor.extractProperties("match (n) where id(n) = (1) create (m { name: 'Andres'}) set n.age = 19"), hasItems("name", "age"));
    }

    @Test
    public void testIgnoreTrailingSemicolon() throws Exception
    {
        cypherQueryExecutor.cypherQuery( "create (n {});\n " ,null);
    }

    @Test(expected = SyntaxException.class)
	@Ignore
    public void testAdhereToCypherVersion16() throws Exception {
        cypherQueryExecutor.cypherQuery("match (n) where id(n) = (1) match n-[:A|B]-() return n","1.6");
    }

    @Test(expected = SyntaxException.class)
    public void testAdhereToCypherVersion17() throws Exception {
        cypherQueryExecutor.cypherQuery("create (n {})","1.7");
    }

    @Test @Ignore("only 1.9 and 2.0")
    public void testAdhereToCypherVersion18() throws Exception {
        cypherQueryExecutor.cypherQuery("create (n {})","1.8");
    }
    @Test
    public void testAdhereToNoCypherVersion() throws Exception {
        cypherQueryExecutor.cypherQuery("create (n:Foo {})",null);
    }
    @Test
    public void testAdhereToNoCypherVersion2() throws Exception {
        cypherQueryExecutor.cypherQuery("create (n:Foo {name:'Joe'})",null);
    }

    @Test
    public void testWorksWithMerge() throws Exception {
        final CypherQueryExecutor.CypherResult result = cypherQueryExecutor.cypherQuery("merge (n:Label {name:'foobar'}) return n.name", null);
        assertEquals(1,result.getRowCount());
        final Object value = result.getRows().iterator().next().get("n.name");
        assertEquals("foobar", value);
    }

    @Test
    public void testWorksWithCypherPrefix() throws Exception {
        CypherQueryExecutor.CypherResult result = cypherQueryExecutor.cypherQuery("cypher 2.3 match (n) return count(*) as cnt", "cypher 2.3");
        assertEquals(asList("cnt"),result.getColumns());
        assertEquals(1, result.getRowCount());
    }

    @Test
    public void testPrettifyQuery() throws Exception {
        final String pretty = cypherQueryExecutor.prettify("match (n) where id(n) = (1) match n--> () return n");
        assertEquals("MATCH (n)\n" +
                " WHERE id(n)=(1)\n" +
                " MATCH n-->()\n" +
                " RETURN n",pretty);
    }

    @Test
    public void testCypherQuery() throws Exception {
        try (Transaction tx = gdb.beginTx()) {
            final CypherQueryExecutor.CypherResult result = cypherQueryExecutor.cypherQuery("match (n) where id(n) = (0) return n", null);
            assertEquals(asList("n"), result.getColumns());
            assertTrue(result.getText(), result.getText().contains("Node[0]"));
            for (Map<String, Object> row : result) {
                assertEquals(1, row.size());
                assertEquals(true, row.containsKey("n"));
                assertEquals(refNode, row.get("n"));
            }
            tx.success();
        }
    }

    @Test
    public void testToJson() throws Exception {
        gdb.beginTx();
        final Node n1 = gdb.createNode();
        n1.setProperty("name","n1");
        n1.setProperty("age",10);
        final Relationship rel = refNode.createRelationshipTo(n1, DynamicRelationshipType.withName("REL"));
        rel.setProperty("name","rel1");
        final CypherQueryExecutor.CypherResult result = cypherQueryExecutor.cypherQuery("match (n) where id(n) = (0) match p=n-[r]->m return p,n,r,m", null);
        System.out.println(result);
        final List<Map<String,Object>> json = result.getJson();
        System.out.println(new Gson().toJson(json));
        assertEquals(1, json.size());
        final Map<String, Object> row = json.get(0);
        assertEquals(4, row.size());
        final Map node1 = (Map) row.get("n");
        assertEquals(1, node1.size());
        assertEquals(0L, node1.get("_id"));
        final Map node2 = (Map) row.get("m");
        assertEquals(3, node2.size());
        assertEquals(1L, node2.get("_id"));
        assertEquals("n1", node2.get("name"));
        assertEquals(10, node2.get("age"));

        final Map rel1 = (Map) row.get("r");
        assertEquals(5, rel1.size());
        assertEquals(0L, rel1.get("_id"));
        assertEquals("rel1", rel1.get("name"));
        assertEquals("REL", rel1.get("_type"));
        assertEquals(0L, rel1.get("_start"));
        assertEquals(1L, rel1.get("_end"));

        final List path = (List) row.get("p");
        assertEquals(3, path.size());
        final Map pathNode1 = (Map) path.get(0);
        assertEquals(0L, pathNode1.get("_id"));
        final Map pathRel1 = (Map) path.get(1);
        assertEquals("rel1", pathRel1.get("name"));
        final Map pathNode2 = (Map) path.get(2);
        assertEquals(10, pathNode2.get("age"));
    }

    @Test
    public void testReplaceIndex() throws Exception {
        String queryAuto="start n=node:node_auto_index(name='foo') return n;";
        assertEquals(queryAuto,cypherQueryExecutor.replaceIndex(queryAuto));
        String queryId="match (n) where id(n) IN [3,4,5] return n;";
        assertEquals(queryId,cypherQueryExecutor.replaceIndex(queryId));
        String queryEmpty="start n=node:(name='foo') return n;";
        assertEquals(queryAuto,cypherQueryExecutor.replaceIndex(queryEmpty));
        String queryPeople="start n=node:people(name='foo') return n;";
        assertEquals(queryAuto,cypherQueryExecutor.replaceIndex(queryPeople));
        String queryPeople2="start n=node:`pe op-le`(name='foo') return n;";
        assertEquals(queryAuto,cypherQueryExecutor.replaceIndex(queryPeople2));
        String queryPeopleAndEmails="start n=node:people(name='foo'),m=node:emails(subject='foo') return n,m;";
        assertEquals("start n=node:node_auto_index(name='foo'),m=node:node_auto_index(subject='foo') return n,m;",cypherQueryExecutor.replaceIndex(queryPeopleAndEmails));
        String queryPeopleAndEmailsWith="start n=node:people(name='foo') with n start m=node:emails(subject='foo') return n,m;";
        assertEquals("start n=node:node_auto_index(name='foo') with n start m=node:node_auto_index(subject='foo') return n,m;",cypherQueryExecutor.replaceIndex(queryPeopleAndEmailsWith));

        String queryRelAuto="start r=relationship:relationship_auto_index(name='foo') return r;";
        String queryRels="start r=relationship:`lo v e s`(name='foo') return r;";
        assertEquals(queryRelAuto,cypherQueryExecutor.replaceIndex(queryRels));

    }

    @Test
    @Ignore("never profile")
    public void testDontProfileUnionCheck() throws Exception {
        assertFalse(cypherQueryExecutor.canProfileQuery("match (n)  return n UNION match (n)  return n"));
        assertFalse(cypherQueryExecutor.canProfileQuery("match (n)  return n \nUNION\n match (n)  return n"));
        assertFalse(cypherQueryExecutor.canProfileQuery("match (n)  return n \nunion\n match (n)  return n"));
        assertFalse(cypherQueryExecutor.canProfileQuery("match (n)  return n"));
    }

    @Test
    public void testDontProfileUnion() throws Exception {
        CypherQueryExecutor.CypherResult result = cypherQueryExecutor.cypherQuery("match (n)  return n UNION match (n)  return n", null);
        assertEquals(1,result.getRowCount());
    }
}
