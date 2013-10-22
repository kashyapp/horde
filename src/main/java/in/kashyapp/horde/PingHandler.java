package in.kashyapp.horde;

import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * User: kashyapp
 * Time: 08/07/13 4:35 PM
 */
public class PingHandler extends AbstractTargetHandler {
    Logger log = LoggerFactory.getLogger(PingHandler.class);

    public PingHandler() {
        super("/ping");
    }

    @Override
    public void handle(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("baseRequest: {}", baseRequest);
        log.info("request: {}", request);

        HttpSession session = request.getSession();
        log.info("Session: {} {}", session.getCreationTime(), session);

        response.getOutputStream().println("PONG");
    }
}
