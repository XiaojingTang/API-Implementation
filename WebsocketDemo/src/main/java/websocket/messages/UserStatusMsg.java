package websocket.messages;

import java.util.List;

public class UserStatusMsg extends BaseMessage {

    public static final String TYPE = "USER_STATUS";

    private List<Account> accounts;

    public UserStatusMsg() {
        super(TYPE);
    }

    public UserStatusMsg(List<Account> accounts) {
        this();
        this.accounts = accounts;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

}
