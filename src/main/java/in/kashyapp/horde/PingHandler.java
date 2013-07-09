package in.kashyapp.horde;

import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: kashyapp
 * Time: 08/07/13 4:35 PM
 */
public class PingHandler extends AbstractTargetHandler {
    public PingHandler() {
        super("/ping");
    }

    @Override
    public void handle(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getOutputStream().println("PONG");
    }
}
