package websocket;

public interface EngineIncoming {

    void login(EngineOutgoing user);

    void logout(String user);

    void createAccount(String user, String accountName, String currency);

    void fundAccount(String user, String accountName, double amount);

    void deleteAccount(String user, String accountName);

    void incomingRequestForTransfer(String fromAccount, String toAccount, String amount);
}
