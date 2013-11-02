package org.neo4j.training.backend;

/**
 * @author Michael Hunger @since 02.11.13
 */
public interface GraphStorage {
    GraphInfo find(String id);

    void update(GraphInfo info);

    GraphInfo create(GraphInfo info);

    void delete(String id);
}
