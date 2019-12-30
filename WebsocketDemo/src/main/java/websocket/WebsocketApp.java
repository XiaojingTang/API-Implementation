package websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import websocket.application.ApiIncoming;
import websocket.application.EngineIncoming;
import websocket.persistence.H2Persister;
import websocket.persistence.Persister;
import websocket.session.SessionManager;
import websocket.wsserver.WebSocketServer;

public class WebsocketApp {
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(WebsocketApp.class);
        Persister persister = new H2Persister();
        EngineIncoming engineIncoming = new ApiIncoming(persister);
        SessionManager sessionManager = new SessionManager(engineIncoming);
        int port = 8085;

        logger.info("Listening on port " + port);
        new WebSocketServer(port, sessionManager);
    }
}
