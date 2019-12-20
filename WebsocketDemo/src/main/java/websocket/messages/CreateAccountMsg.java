package websocket.messages;

public class CreateAccountMsg extends BaseMessage {
    public static final String TYPE = "CREATE_ACCOUNT";

    private String userName;
    private String accountName;

    public CreateAccountMsg() {
        super(TYPE);
    }

    public CreateAccountMsg(String userName, String accountName) {
        this();
        this.userName = userName;
        this.accountName = accountName;
    }

    public String getUserName() {
        return userName;
    }

    public String getAccountName() {
        return accountName;
    }
}
