package websocket.messages;

public class LogoutMsg extends BaseMessage {
    public static final String TYPE = "LOGOUT";
    private String userName;

    public LogoutMsg() {
        super(TYPE);
    }

    public LogoutMsg(String userName) {
        this();
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
