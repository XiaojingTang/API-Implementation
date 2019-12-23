package websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import websocket.messages.AllAccountsMsg;
import websocket.messages.AllFundRecordsMsg;
import websocket.messages.AllTransferRecordsMsg;
import websocket.messages.NotificationMsg;
import websocket.session.SessionManager;

public class ApiOutgoing implements EngineOutgoing {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private SessionManager sessionManager;
    private String name;

    public ApiOutgoing(SessionManager sessionManager, String name) {
        this.sessionManager = sessionManager;
        this.name = name;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void sendNotification(NotificationMsg notificationMsg) {

    }

    @Override
    public void sendAllAccounts(AllAccountsMsg allAccountsMsg) {

    }

    @Override
    public void sendAllFundRecords(AllFundRecordsMsg allFundRecordsMsg) {

    }

    @Override
    public void sendAllTransferRecords(AllTransferRecordsMsg allTransferRecordsMsg) {

    }
}