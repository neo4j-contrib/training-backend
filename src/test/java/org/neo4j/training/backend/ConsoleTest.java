package org.neo4j.training.backend;

import org.junit.Ignore;
import org.neo4j.test.ImpermanentGraphDatabase;
import org.neo4j.test.TestGraphDatabaseFactory;

/**
 * @author mh
 * @since 05.06.12
 */
@Ignore
public class ConsoleTest {
    public static void main(String[] args) throws Exception {
        final Backend backend = new Backend(DatabaseInfo.expose(new TestGraphDatabaseFactory().newImpermanentDatabase()));
        backend.start(9000);
        backend.join();
    }
}
