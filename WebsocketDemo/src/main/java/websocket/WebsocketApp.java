package websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import websocket.messages.CreateAccountMsg;
import websocket.server.WebSocketServer;
import websocket.session.SessionManager;

public class WebsocketApp {
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(WebsocketApp.class);
        PersistenceLayer persistenceLayer = new H2Persister();
        EngineIncoming engineIncoming = new ApiIncoming(persistenceLayer);
        int port = 8082;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        SessionManager sessionManager = new SessionManager(engineIncoming);
//            initializationTestCases(apiIncoming, persistenceLayer);

        CreateAccountMsg createAccountMsg = new CreateAccountMsg("Jean", "USD");
//     FundAccountMsg fundAccountMsg = new FundAccountMsg(long accountId, String transCurrency, double amount, String date) {
        System.out.println(gson.toJson(createAccountMsg));
        logger.info("Listening on port " + port);
        new WebSocketServer(port, sessionManager);
    }

    private static void initializationTestCases(EngineIncoming apiIncoming, PersistenceLayer persistenceLayer) {
        persistenceLayer.clearData();
        persistenceLayer.createTables();
    }
}
