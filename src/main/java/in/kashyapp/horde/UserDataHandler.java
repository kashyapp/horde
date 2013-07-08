package in.kashyapp.horde;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpHeaderValues;
import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.SocketException;
import java.util.Map;

/**
 * User: kashyapp
 * Time: 08/07/13 9:59 AM
 */
public class UserDataHandler extends AbstractHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(UserDataHandler.class);
    private static final String TARGET = "/userData";

    private static final ObjectMapper mapper = new ObjectMapper();
    private final JedisFactory jedisFactory;

    public UserDataHandler(JedisFactory jedisFactory) {
        this.jedisFactory = jedisFactory;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (! TARGET.equals(target)) {
            return;
        }

        Jedis jedis = jedisFactory.get();

        response.setContentType(MimeTypes.TEXT_JSON_UTF_8);
        response.setHeader(HttpHeaders.CACHE_CONTROL, HttpHeaderValues.NO_CACHE);

        final String userId = request.getParameter("_u");

        try {
            final Map<String,String> assets = jedis.hgetAll(userId);
            mapper.writeValue(response.getOutputStream(), assets);
            baseRequest.setHandled(true);
        } catch (JedisConnectionException e) {
            // if we disconnect here, it will reconnect on the next call
            jedis.disconnect();
            // TODO: ignoring for now
            // could be retried
            log.error("Error with redis", e);
        }
    }
}
