package websocket;

import com.etale.otc.messages.InitializationMessage;

public interface EngineOutgoing {

    String getName();

    void initialization(InitializationMessage initializationMessage);

    void updateUserPresence(String user, boolean joined);

    void login(EngineOutgoing user);

    void logout(String user);

    void createAccount(String user, String accountName);

    void deleteAccount(String user, String accountName);

    void incomingRequestForTransfer(String fromAccount, String toAccount, String amount);

    void outgoingAcceptanceConfirmation(String originator, String rfqId);

    void outgoingSendMessage(String sender, String recipient, String message);
}
