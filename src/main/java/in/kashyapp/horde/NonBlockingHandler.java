package in.kashyapp.horde;

import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: kashyapp
 * Time: 09/07/13 8:50 PM
 */
public class NonBlockingHandler extends AbstractTargetHandler {
    private final JedisFactory jedisFactory;

    public NonBlockingHandler(JedisFactory jedisFactory) {
        super("/nonblock");
        this.jedisFactory = jedisFactory;
    }

    @Override
    public void handle(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }
}
