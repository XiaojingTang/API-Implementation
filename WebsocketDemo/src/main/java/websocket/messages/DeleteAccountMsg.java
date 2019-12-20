package websocket.messages;

public class DeleteAccountMsg extends BaseMessage {
    public static final String TYPE = "DELETE_ACCOUNT";

    private String userName;
    private String accountName;

    public DeleteAccountMsg() {
        super(TYPE);
    }

    public DeleteAccountMsg(String userName, String accountName) {
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

    @Override
    public String toString() {
        return "DeleteAccountMsg{" +
                "userName='" + userName + '\'' +
                ", accountName='" + accountName + '\'' +
                ", type='" + type + '\'' +
                "} " + super.toString();
    }
}
