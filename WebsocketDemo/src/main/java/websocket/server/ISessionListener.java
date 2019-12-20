package websocket.server;

import org.eclipse.jetty.websocket.api.Session;

public interface ISessionListener {
    void onConnect(Session sess);

    void onMessage(String msg);

    void onClose(int status, String reason);

    void onError(String cause);
}
