package org.neo4j.training.backend;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import spark.servlet.SparkApplication;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author mh
 * @since 05.06.12
 */
public class BackendFilterTest {

    static DatabaseInfo databaseInfo;

    @Test
    public void testGetDatabaseFactory() throws Exception {
        final FilterConfig config = mock(FilterConfig.class);
        final ServletContext context = mock(ServletContext.class);
        final GraphDatabaseService database = mock(GraphDatabaseService.class);
        when(context.getAttribute(argThat(is(BackendFilter.DATABASE_ATTRIBUTE)))).thenReturn(DatabaseInfo.sandbox(database));
        when(config.getServletContext()).thenReturn(context);
        when(config.getInitParameter(argThat(is("applicationClass")))).thenReturn(TestApplication.class.getName());
        new BackendFilter().init(config);
        assertThat(databaseInfo.getDatabase(),is(database));
        assertThat(databaseInfo.isSandbox(),is(true));

    }

    public static class TestApplication implements SparkApplication {
        @Override
        public void init() {
            databaseInfo = BackendFilter.getDatabase();
        }
    }
}
