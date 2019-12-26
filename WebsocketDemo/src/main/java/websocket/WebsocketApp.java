package websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import websocket.application.ApiIncoming;
import websocket.application.EngineIncoming;
import websocket.messages.CreateAccountMsg;
import websocket.messages.FundAccountMsg;
import websocket.messages.ListAllAccountsMsg;
import websocket.messages.TransferFundMsg;
import websocket.persistence.H2Persister;
import websocket.persistence.Persister;
import websocket.session.SessionManager;
import websocket.wsserver.WebSocketServer;

public class WebsocketApp {
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(WebsocketApp.class);
        Persister persister = new H2Persister();
        EngineIncoming engineIncoming = new ApiIncoming(persister);
        int port = 8085;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        SessionManager sessionManager = new SessionManager(engineIncoming);

        CreateAccountMsg createAccountMsg = new CreateAccountMsg("Jean", "USD");
        System.out.println(gson.toJson(createAccountMsg));
        ListAllAccountsMsg listAllAccountsMsg = new ListAllAccountsMsg();
        System.out.println(gson.toJson(listAllAccountsMsg));

        FundAccountMsg fundAccountMsg = new FundAccountMsg(1, "DZD", 200, "2019-01-01");
        System.out.println(gson.toJson(fundAccountMsg));
        TransferFundMsg transferFundMsg = new TransferFundMsg(1, 2, "USD", 100, "2019-03-01");
        System.out.println(gson.toJson(transferFundMsg));
        logger.info("Listening on port " + port);
        new WebSocketServer(port, sessionManager);
    }
}
