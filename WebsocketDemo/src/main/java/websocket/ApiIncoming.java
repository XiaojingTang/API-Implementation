package websocket;

public class ApiIncoming implements EngineIncoming {
    @Override
    public void login(EngineOutgoing user) {

    }

    @Override
    public void logout(String user) {

    }

    @Override
    public void createAccount(String user, String currency) {

    }

    @Override
    public void fundAccount(long accountId, String transCurrency, double amount, String date) {

    }

    @Override
    public void transferFund(long fromAccountId, long toAccountId, String transCurrency, double amount, String date) {

    }

    @Override
    public void listAllAccounts() {

    }

    @Override
    public void listAllFundRecords() {

    }

    @Override
    public void listAllTransferRecords() {

    }
}
