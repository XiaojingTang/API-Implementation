package websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import websocket.session.SessionManager;

public class ApiOutgoing implements EngineOutgoing {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private SessionManager sessionManager;
    private String name;

    public ApiOutgoing(SessionManager sessionManager, String name) {
        this.sessionManager = sessionManager;
        this.name = name;
    }

}