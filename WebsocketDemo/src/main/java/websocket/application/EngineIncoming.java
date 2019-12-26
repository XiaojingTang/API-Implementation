package websocket.application;

public interface EngineIncoming {
    void setOutgoing(EngineOutgoing outgoing);

    void createAccount(String user, String currency);

    void fundAccount(long accountId, String transCurrency, double amount, String date);

    void transferFund(long fromAccountId, long toAccountId, String transCurrency, double amount, String date);

    void listAllAccounts();

    void listAllFundRecords();

    void listAllTransferRecords();
}
