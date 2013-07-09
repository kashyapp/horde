package in.kashyapp.horde;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: kashyapp
 * Time: 09/07/13 8:51 PM
 */
public abstract class AbstractTargetHandler extends AbstractHandler {
    private final String target;

    public AbstractTargetHandler(String target) {
        this.target = target;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (this.target.equals(target)) {
            handle(baseRequest, request, response);
            baseRequest.setHandled(true);
        }
    }

    protected abstract void handle(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
