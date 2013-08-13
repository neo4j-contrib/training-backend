package org.neo4j.training.backend;

import org.neo4j.kernel.lifecycle.LifecycleException;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mh
 * @since 09.04.12
 */
class SessionService {
    public static final String SESSION_HEADER = "X-Session";

    private static DatabaseInfo databaseInfo;

    private static Map<String, Neo4jService> sessions=new LinkedHashMap<>();

    public static void setDatabaseInfo(DatabaseInfo databaseInfo) {
        SessionService.databaseInfo = databaseInfo;
    }

    public static void reset(final HttpServletRequest httpRequest) {
        String sessionId = getSessionId(httpRequest);
        if (sessionId==null) return;
        Neo4jService service = sessions.get(sessionId);
        if (service == null) return;
        try {
            service.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessions.remove(sessionId);
        }
    }

    public static void cleanSession(String sessionId) {
        Neo4jService service = sessions.get(sessionId);
        if (service == null) return;
        try {
            service.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessions.remove(sessionId);
        }
    }

    public static Neo4jService getService(final HttpServletRequest request,boolean create) {
        String sessionId = getSessionId(request);
        try {
            Neo4jService service = sessions.get(sessionId);
            if (service != null) return service;
            if (!create) throw new IllegalStateException("No Service for session "+sessionId+" available");
            service = databaseInfo.shouldCreateNew() ? new Neo4jService() : new Neo4jService(databaseInfo.getDatabase());
            if (databaseInfo.shouldImport()) {
                service.initializeFrom(SubGraph.from(databaseInfo.getDatabase()));
            }
            sessions.put(sessionId, service);
            return service;
        } catch (LifecycleException lce) {
            reset(request);
            cleanSessions();
            throw new RuntimeException(lce);
        } catch (OutOfMemoryError oom) {
            reset(request);
            cleanSessions();
            throw new RuntimeException(oom);
        } catch (Throwable t) {
            reset(request);
            throw new RuntimeException(t);
        }
    }

    public static void cleanSessions() {
        Iterator<Map.Entry<String,Neo4jService>> it = sessions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Neo4jService> entry = it.next();
            Neo4jService service = entry.getValue();
            if (service !=null) {
                try {
                    service.stop();
                } catch (Exception e) {
                    // ignore
                }
            }
            it.remove();
        }
    }

    private static String getSessionId(HttpServletRequest request) {
        return request.getHeader(SESSION_HEADER);
    }

    public static DatabaseInfo getDatabaseInfo() {
        return databaseInfo;
    }
}
