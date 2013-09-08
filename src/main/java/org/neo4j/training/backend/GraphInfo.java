package org.neo4j.training.backend;

import org.neo4j.graphdb.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mh
 * @since 30.05.12
 */
public class GraphInfo {
    private final String id;
    private final String init;
    private final String query;
    private final String message;
    private final String version;
    private final boolean noRoot;
    private String history;

    public GraphInfo(Node node) {
        this.id = (String) node.getProperty("id");
        this.init = (String) node.getProperty("init",null);
        this.query = (String) node.getProperty("query",null);
        this.message = (String) node.getProperty("message",null);
        this.version = (String) node.getProperty("version",null);
        this.history = (String) node.getProperty("history",null);
        this.noRoot = (Boolean) node.getProperty("no_root",false);
    }

    GraphInfo(String id, String init, String query, String message) {
        this(id,init,query,message,null);
    }
    GraphInfo(String id, String init, String query, String message, String version) {
        this(id,init,query,message,version,false);
    }

    GraphInfo(String id, String init, String query, String message, String version, boolean noRoot) {
        this(id,init,query,message,version,noRoot,"");
    }
    GraphInfo(String id, String init, String query, String message, String version, boolean noRoot, String history) {
        this.id = id;
        this.init = init;
        this.query = query;
        this.message = message;
        this.version = version;
        this.noRoot = noRoot;
        this.history = history;
    }

    public Map<String,Object> toMap() {
        final HashMap<String, Object> result = new HashMap<String, Object>();
        map(result, "id", id);
        map(result, "init", init);
        map(result, "query", query);
        map(result, "message", message);
        map(result, "version", version);
        map(result, "history", history,"");
        if (noRoot) result.put("no_root",true);
        return result;
    }

    private void map(Map<String, Object> result, final String key, final String value) {
        if (value!=null && !value.trim().isEmpty()) result.put(key, value);
    }
    private void map(Map<String, Object> result, final String key, final String value,String defaultValue) {
        if (value!=null && !value.trim().isEmpty()) result.put(key, value);
        else result.put(key,defaultValue);
    }

    public String getId() {
        return id;
    }

    public String getInit() {
        return init;
    }

    public String getQuery() {
        return query;
    }

    public String getHistory() {
        return history;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("GraphInfo{id='%s', init='%s', query='%s', message='%s', version='%s',history='%s', noRoot=%s}", id, init, query, message, version, history, noRoot);
    }

    public GraphInfo newQuery(String query) {
        return new GraphInfo(id,init,query,message,version,noRoot,history);
    }

    public String getVersion() {
        return version;
    }

    public boolean hasRoot() {
        return !noRoot;
    }

    public GraphInfo noRoot() {
        return new GraphInfo(id,init,query,message,version,true,history);
    }

    public static GraphInfo from(Map input) {
        boolean noRoot = input.containsKey("no_root") && input.get("no_root").toString().equals("true");
        return new GraphInfo(
                (String)input.get("id"),(String)input.get("init"),
                (String)input.get("query"),(String)input.get("message"),
                (String)input.get("version"),noRoot);
    }

    public GraphInfo withId(String id) {
        return new GraphInfo(id,init,query,message,version,noRoot);
    }

    public GraphInfo withHistory(String history) {
        return new GraphInfo(id,init,query,message,version,true,history);
    }
}
