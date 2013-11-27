package org.neo4j.training.backend;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.webapp.WebAppContext;
import org.neo4j.ext.udc.UdcSettings;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;
import org.slf4j.Logger;

public class Backend
{

    private static final String WEBAPP_LOCATION = "src/main/webapp/";
    public static final int REQUEST_TIME_LIMIT = 5 * 1000;
    public static final int MAX_OPS_LIMIT = 10000;
    private Server server;
    private final DatabaseInfo databaseInfo;
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(Backend.class);

    public Backend(DatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
    }

    public static void main(String[] args) throws Exception
    {
        System.setProperty(UdcSettings.udc_source.name(),"backend");
        int port = (args.length>0) ? Integer.parseInt(args[0]): getPort();
        boolean expose = args.length>2 && args[2].equalsIgnoreCase("expose");
        GraphDatabaseService database = null;
        final Backend backend = expose ? Backend.expose(database) : Backend.sandbox(database);
        backend.start(port);
        backend.join();
    }

    public static Backend sandbox(GraphDatabaseService database) {
        return new Backend(DatabaseInfo.sandbox(database));
    }

    public static Backend expose(GraphDatabaseService database) {
        return new Backend(DatabaseInfo.expose(database));
    }

    public void start(int port) throws Exception {
        LOG.warn("Port used: " + port + " location " + WEBAPP_LOCATION + " " + databaseInfo.toString());
        server = new Server(port);
        WebAppContext root = new WebAppContext();
        root.setContextPath("/");
        root.setDescriptor(WEBAPP_LOCATION + "/WEB-INF/web.xml");
        root.setResourceBase(WEBAPP_LOCATION);
        root.setParentLoaderPriority(true);
        root.setAttribute(BackendFilter.DATABASE_ATTRIBUTE, databaseInfo);
//        setupRequestLimits(root, REQUEST_TIME_LIMIT, MAX_OPS_LIMIT);
        final HandlerList handlers = new HandlerList();
        final Handler resourceHandler = createResourceHandler("/console_assets", WEBAPP_LOCATION);
        handlers.setHandlers(new Handler[]{resourceHandler,root});
        server.setHandler(handlers);
        server.start();
    }

    private Handler createResourceHandler(String context, String resourceBase) {
        WebAppContext ctx = new WebAppContext();
        ctx.setContextPath(context);
        ctx.setResourceBase(resourceBase);
        ctx.setParentLoaderPriority(true);
        return ctx;
    }

    private void setupRequestLimits(WebAppContext root, Integer limit, int maxOps) {
        if (limit == null) return;
        GuardingRequestFilter requestTimeLimitFilter = new GuardingRequestFilter(limit, maxOps);
        root.addFilter(new FilterHolder(requestTimeLimitFilter), "/backend/*", FilterMapping.REQUEST);
    }

    public void join() throws InterruptedException {
        server.join();
    }


    public void stop() throws Exception {
        server.stop();
    }

    private static int getPort() {
        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            return 8080;
        }
        return Integer.parseInt(webPort);
    }
}

