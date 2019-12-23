package websocket.messages;

public class CreateAccountMsg extends BaseMessage {
    public static final String TYPE = "CREATE_ACCOUNT";

    private String user;
    private String currency;

    public CreateAccountMsg() {
        super(TYPE);
    }

    public CreateAccountMsg(String user, String currency) {
        this();
        this.user = user;
        this.currency = currency;
    }

    public String getUser() {
        return user;
    }

    public String getCurrency() {
        return currency;
    }
}
