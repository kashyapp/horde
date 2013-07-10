package in.kashyapp.horde;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.http.HttpHeaderValues;
import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: kashyapp
 * Time: 09/07/13 8:50 PM
 */
public class NonBlockingHandler extends AbstractTargetHandler {
    private final Logger log = LoggerFactory.getLogger(NonBlockingHandler.class);
    private final RedisPipeline pipeline;

    public NonBlockingHandler(RedisPipeline pipeline) {
        super("/nonblock");
        this.pipeline = pipeline;
    }

    @Override
    public void handle(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType(MimeTypes.TEXT_JSON_UTF_8);
        response.setHeader(HttpHeaders.CACHE_CONTROL, HttpHeaderValues.NO_CACHE);

        final String userId = request.getParameter("_u");

        final Continuation continuation = ContinuationSupport.getContinuation(request);
        continuation.suspend(response);

        pipeline.request(userId, continuation);
    }
}
