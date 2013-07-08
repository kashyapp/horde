package in.kashyapp.horde;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: kashyapp
 * Time: 08/07/13 4:35 PM
 */
public class PingHandler extends AbstractHandler {

    private static final String PING = "/ping";

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!PING.equals(target)) {
            return;
        }

        response.getOutputStream().println("PONG");
        baseRequest.setHandled(true);
    }
}
