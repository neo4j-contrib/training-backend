package org.neo4j.training.backend;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.neo4j.helpers.collection.MapUtil.map;

public class Neo4jServiceTest {

    public static final String QUERY1 = "start n=node(*) return count(*)";
    public static final String QUERY2 = "start n=node(*) return count(n)";

    @Test
    public void testRecordHistory() throws Throwable {
        Neo4jService neo4jService = new Neo4jService();
        neo4jService.cypherQuery(QUERY1);
        assertEquals(QUERY1, joinHistory(neo4jService));
        neo4jService.cypherQuery(QUERY1);
        assertEquals(QUERY1, joinHistory(neo4jService));
        neo4jService.cypherQuery(QUERY2);
        assertEquals(QUERY1 + ";" + QUERY2, joinHistory(neo4jService));
        neo4jService.cypherQuery(QUERY1);
        assertEquals(QUERY2 + ";" + QUERY1, joinHistory(neo4jService));
    }

    private String joinHistory(Neo4jService neo4jService) {
        return Util.join(neo4jService.getHistory(), ";");
    }

    @Test
    public void testRunsShutdownHook() throws Throwable {
        Neo4jService neo4jService = new Neo4jService();
        TestShutdownHook shutdownHook = new TestShutdownHook();
        neo4jService.setShutdownHook(shutdownHook);
        neo4jService.stop();
        assertEquals(true,shutdownHook.ranShutdown.get());
    }

    @Test
    public void testRunsNoShutdownHookWithoutChange() throws Throwable {
        final GraphStorage storage = Mockito.mock(GraphStorage.class);
        BackendService service = new BackendService() {
            @Override
            protected GraphStorage createGraphStorage() {
                return storage;
            }
        };
        Neo4jService neo4jService = new Neo4jService();
        service.init(neo4jService,map("init","create n"),"session-id");
        neo4jService.stop();
        Mockito.verify(storage,Mockito.never()).update(Mockito.any(GraphInfo.class));
        Mockito.verify(storage,Mockito.never()).create(Mockito.any(GraphInfo.class));
    }

    @Test
    public void testRunsShutdownHookWithChange() throws Throwable {
        final GraphStorage storage = Mockito.mock(GraphStorage.class);
        BackendService service = new BackendService() {
            @Override
            protected GraphStorage createGraphStorage() {
                return storage;
            }
        };
        Neo4jService neo4jService = new Neo4jService();
        service.init(neo4jService,map("init","create n"),"session-id");
        service.execute(neo4jService,null,"create n",null);
        neo4jService.stop();
        Mockito.verify(storage,Mockito.never()).update(Mockito.any(GraphInfo.class));
        Mockito.verify(storage,Mockito.times(1)).create(Mockito.any(GraphInfo.class));
    }

    private static class TestShutdownHook implements Neo4jService.ShutdownHook {
        final AtomicBoolean ranShutdown = new AtomicBoolean();

        public void shutdown(Neo4jService neo4jService) {
            ranShutdown.set(true);
        }
    }
}
