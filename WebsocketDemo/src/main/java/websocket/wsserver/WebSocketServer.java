package websocket.wsserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebSocketServer {
    private static Logger logger = LogManager.getLogger(WebSocketServer.class);

    private Server server;
    private ISessionManager sessionManager;

    public WebSocketServer(int port, ISessionManager sessionManager) {
        this.sessionManager = sessionManager;

        server = new Server(port);

        org.eclipse.jetty.websocket.server.WebSocketHandler wsHandler = new org.eclipse.jetty.websocket.server.WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.setCreator(new WebSocketHandlerCreator());
            }
        };

        server.setHandler(new HealthCheckHandler());
        server.insertHandler(wsHandler);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public class HealthCheckHandler extends AbstractHandler {
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            response.getWriter().println("<h1>I'm alive</h1>");
        }
    }

    private class WebSocketHandlerCreator implements WebSocketCreator {
        @Override
        public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
            return new WebSocketHandler(sessionManager.getSessionListener());
        }
    }
}
