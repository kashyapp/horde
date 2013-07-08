package in.kashyapp.horde;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;

/**
 * User: kashyapp
 * Time: 07/07/13 7:21 PM
 */
public class HordeService {

    private final Server server;

    public HordeService() {
        server = new Server(8080);

        server.setSendDateHeader(false);
        server.setSendServerVersion(false);
        server.setStopAtShutdown(true);
        server.setGracefulShutdown(500);
        server.setThreadPool(buildThreadPool());

        server.setHandler(buildHandler());
    }

    private void start() throws Exception {
        server.start();
    }

    private ThreadPool buildThreadPool() {
        final int poolSize = 8;
        final QueuedThreadPool qtp = new QueuedThreadPool(poolSize);
        qtp.setMinThreads(poolSize);
        qtp.setMaxQueued(500);
        return qtp;
    }

    private Handler buildHandler() {
        final HandlerList handlerList = new HandlerList();
        handlerList.addHandler(new PingHandler());
        handlerList.addHandler(new UserDataHandler(new ThreadLocalJedisFactory()));
        return handlerList;
    }

    public static void main(String[] args) throws Exception {
        final HordeService service = new HordeService();
        service.start();
    }
}
