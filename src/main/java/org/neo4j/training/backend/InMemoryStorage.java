package org.neo4j.training.backend;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Hunger @since 02.11.13
 */
public class InMemoryStorage implements GraphStorage {

    private final Map<String, GraphInfo> data=new HashMap<>();

    @Override
    public GraphInfo find(String id) {
        return data.get(id);
    }

    @Override
    public void update(GraphInfo info) {
        String id = info.getId();
        GraphInfo found = data.get(id);
        if (found==null) create(info);
        else {
            data.put(id, found.withHistory(info.getHistory()).newQuery(info.getQuery()));
        }
    }

    @Override
    public GraphInfo create(GraphInfo info) {
        data.put(info.getId(),info);
        return info;
    }

    @Override
    public void delete(String id) {
        data.remove(id);
    }
}
