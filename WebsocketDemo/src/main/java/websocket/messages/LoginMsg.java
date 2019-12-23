package websocket.messages;

public class LoginMsg extends BaseMessage {
    public static final String TYPE = "LOGIN";

    private String userName;
    private String password;

    public LoginMsg() {
        super(TYPE);
    }

    public LoginMsg(String userName, String password) {
        this();
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
