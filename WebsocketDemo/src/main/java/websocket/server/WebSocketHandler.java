package websocket.server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class WebSocketHandler extends WebSocketAdapter {
    private ISessionListener handler;

    public WebSocketHandler(ISessionListener handler) {
        this.handler = handler;
    }

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        handler.onConnect(sess);
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        handler.onMessage(message);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        handler.onClose(statusCode, reason);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        handler.onError(cause.getMessage());
    }
}
