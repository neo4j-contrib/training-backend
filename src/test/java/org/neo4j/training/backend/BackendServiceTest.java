package org.neo4j.training.backend;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BackendServiceTest {

    public static final String ID = "foo";
    public static final String INIT = "bar";

    @Test
    public void testSaveNoStorage() throws Exception {
        BackendService service = new BackendService();
        Map<String,Object> result = service.save(ID, INIT);
        assertEquals("no storage configured",result.get("error"));
    }
    @Test
    public void testSaveCreate() throws Exception {
        final GraphStorage storage = Mockito.mock(GraphStorage.class);
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
    public void testSaveCreateUpdate() throws Exception {
        final GraphStorage storage = Mockito.mock(GraphStorage.class);
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
}
