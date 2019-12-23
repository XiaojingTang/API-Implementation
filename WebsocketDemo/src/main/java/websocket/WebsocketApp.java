package websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import websocket.server.WebSocketServer;
import websocket.session.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebsocketApp {
    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(WebsocketApp.class);
        Persister persister = new H2Persister();
        EngineIncoming otcEngineIncoming = new OTCEngineIncoming(persister);
        int port = 8083;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        try {
            ExchangeConfig.configureFromResource("exchange_config.properties");
            ExchangeConfig econfig = ExchangeConfig.getInstance();

            CommonConfigMsg commonConfigMsg = ExchangeConfig.buildCommonConfig(econfig);
            List<String> exchanges = new ArrayList<>(econfig.getExchangeNames());

            SessionManager sessionManager = new SessionManager(otcEngineIncoming);

//            B2C2Bot b2C2Bot = new B2C2Bot(otcSessionManager, "B2C2Bot");
            initializationTestCases(otcEngineIncoming, persister);

            logger.info("Listening on port " + port);
            new WebSocketServer(port, sessionManager);
        } catch (Exception cex) {
            logger.error(cex.getMessage(), cex);
            System.exit(1);
        }
    }

    private static void initializationTestCases(EngineIncoming engineIncoming, Persister persister) {
        persister.clearData();
        persister.createTables();

        SessionManager otcSessionManager = new SessionManager(engineIncoming);
        EngineOutgoing MATT = new EngineOutgoing(otcSessionManager, "MATT");

        otcEngineIncoming.login(MATT);

        otcEngineIncoming.incomingRequestForQuote(new RFQMsgIncoming("1", "MATT", "BTC-USD", 10, System.currentTimeMillis(), System.currentTimeMillis(), Arrays.asList("GOLDMAN"), Arrays.asList("HSBC")));

        otcEngineIncoming.createGroup("MATT", "Core");
        otcEngineIncoming.addToGroup("MATT", "Core", "GOLDMAN");
        otcEngineIncoming.addToGroup("MATT", "Core", "HSBC");
        otcEngineIncoming.createGroup("MATT", "Related");
        otcEngineIncoming.addToGroup("MATT", "Related", "CITADEL");
        otcEngineIncoming.addToGroup("MATT", "Related", "BNP");

        otcEngineIncoming.logout("MATT");
        otcEngineIncoming.logout("CITADEL");
        otcEngineIncoming.logout("GOLDMAN");
        otcEngineIncoming.logout("HSBC");
    }
}
