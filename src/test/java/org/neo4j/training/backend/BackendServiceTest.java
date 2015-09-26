package org.neo4j.training.backend;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BackendServiceTest {

    public static final String ID = "foo";
    public static final String INIT = "bar";

    @Test
    @Ignore("always has a storage now")
    public void testSaveNoStorage() throws Exception {
        BackendService service = new BackendService();
        Map<String,Object> result = service.save(ID, INIT);
        assertEquals("no storage configured",result.get("error"));
    }
    @Test
    public void testSaveCreate() throws Exception {
        final GraphStorage storage = Mockito.mock(Neo4jGraphStorage.class);
        final BackendService service = new BackendService() {
            @Override
            protected GraphStorage createGraphStorage() {
                return storage;
            }
        };
        Map<String,Object> result = service.save(ID, INIT);
        assertEquals("create",result.get("action"));
        assertEquals(ID,result.get("id"));
        assertEquals(INIT,result.get("init"));
    }

    @Test
    public void testInitUsingJson() throws Throwable {
        final GraphStorage storage = Mockito.mock(Neo4jGraphStorage.class);
        final BackendService service = new BackendService() {
            @Override
            protected GraphStorage createGraphStorage() {
                return storage;
            }
        };
        Neo4jService neo4jService = new Neo4jService();
        Map<String,Object> result = service.execute(neo4jService, SubGraphTest.TEST_JSON_GRAPH_STRING, null, null);
        GraphDatabaseService db = neo4jService.getGraphDatabase();
        try (Transaction tx = db.beginTx()) {
            assertEquals("node0",db.getNodeById(0).getProperty("name"));
            assertEquals("node1",db.getNodeById(1).getProperty("name"));
            assertEquals("rel0",db.getRelationshipById(0).getProperty("name"));
            tx.success();
        }
    }

    @Test
    public void testSaveCreateUpdate() throws Exception {
        final GraphStorage storage = Mockito.mock(Neo4jGraphStorage.class);
        Mockito.when(storage.find(Mockito.eq(ID))).thenReturn(new GraphInfo(ID, INIT,null,null));
        final BackendService service = new BackendService() {
            @Override
            protected GraphStorage createGraphStorage() {
                return storage;
            }
        };
        Map<String,Object> result = service.save(ID, INIT);
        assertEquals("update",result.get("action"));
        assertEquals(ID,result.get("id"));
        assertEquals(INIT,result.get("init"));
    }

    @Test
    public void testParseRestUrl() throws Exception {
        String url = "http://foo:bar@79b160a50.hosted.neo4j.org:7622";
        RestAPI restApi = BackendService.createRestApi(url);
        assertNotNull(restApi);
        assertEquals(url+"/db/data",restApi.getBaseUri());
    }

    @Test
    public void testParseRestUrlWithoutAuth() throws Exception {
        String url = "http://79b160a50.hosted.neo4j.org:7622";
        RestAPI restApi = BackendService.createRestApi(url);
        assertNotNull(restApi);
        assertEquals(url+"/db/data",restApi.getBaseUri());
    }
    @Test
    public void testParseRestUrlForLocalhost() throws Exception {
        String url = "http://localhost:7474";
        RestAPI restApi = BackendService.createRestApi(url);
        assertNotNull(restApi);
        assertEquals(url+"/db/data",restApi.getBaseUri());
    }
}
