package websocket.application;

import websocket.messages.AllAccountsMsg;
import websocket.messages.AllFundRecordsMsg;
import websocket.messages.AllTransferRecordsMsg;
import websocket.messages.NotificationMsg;

public interface EngineOutgoing {

    void sendNotification(NotificationMsg notificationMsg);

    void sendAllAccounts(AllAccountsMsg allAccountsMsg);

    void sendAllFundRecords(AllFundRecordsMsg allFundRecordsMsg);

    void sendAllTransferRecords(AllTransferRecordsMsg allTransferRecordsMsg);
}
