package org.neo4j.training.backend;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

/**
 * @author Michael Hunger @since 02.10.13
 */
public class CypherExportServiceTest {

    private GraphDatabaseService db;
    private Transaction tx;

    @Before
    public void setUp() throws Exception {
        db = new TestGraphDatabaseFactory().newImpermanentDatabase();
        tx = db.beginTx();
    }

    @After
    public void tearDown() throws Exception {
        tx.success();tx.finish();
        db.shutdown();
    }

    @Test
    public void testExportPropertyWithSingleQuote() throws Exception {
        Node node = db.createNode();
        node.setProperty("na'me","foo'bar");
        String exportStatement = new CypherExportService(db).export();
        Assert.assertEquals("create \n(_1  {na'me:\"foo'bar\"})",exportStatement);
    }
}
