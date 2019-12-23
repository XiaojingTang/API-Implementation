package websocket.messages;

import websocket.objects.Account;

import java.util.List;

public class AllAccountsMsg extends BaseMessage {
    public static final String TYPE = "ALL_ACCOUNTS";

    private List<Account> accounts;

    public AllAccountsMsg() {
        super(TYPE);
    }

    public AllAccountsMsg(List<Account> accounts) {
        this();
        this.accounts = accounts;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}
