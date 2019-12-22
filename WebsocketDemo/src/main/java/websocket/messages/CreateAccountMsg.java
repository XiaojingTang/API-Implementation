package websocket.messages;

public class CreateAccountMsg extends BaseMessage {
    public static final String TYPE = "CREATE_ACCOUNT";

    private String user;
    private String currency;
    private Double balance;

    public CreateAccountMsg() {
        super(TYPE);
    }

    public CreateAccountMsg(String user, String currency, Double balance) {
        this();
        this.user = user;
        this.currency = currency;
        this.balance = balance;
    }

    public String getUser() {
        return user;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getBalance() {
        return balance;
    }
}
