package websocket.application;

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

    public ApiOutgoing(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void sendNotification(NotificationMsg notificationMsg) {
        sessionManager.sendMessageToSession(gson.toJson(notificationMsg));
    }

    @Override
    public void sendAllAccounts(AllAccountsMsg allAccountsMsg) {
        sessionManager.sendMessageToSession(gson.toJson(allAccountsMsg));
    }

    @Override
    public void sendAllFundRecords(AllFundRecordsMsg allFundRecordsMsg) {
        sessionManager.sendMessageToSession(gson.toJson(allFundRecordsMsg));
    }

    @Override
    public void sendAllTransferRecords(AllTransferRecordsMsg allTransferRecordsMsg) {
        sessionManager.sendMessageToSession(gson.toJson(allTransferRecordsMsg));
    }
}