package websocket.messages;

public class TransferFundMsg extends BaseMessage {
    public static final String TYPE = "FUND_ACCOUNT";

    private long fromAccountId;
    private long toAccountId;
    private String transCurrency;
    private double amount;
    private String date;

    public TransferFundMsg() {
        super(TYPE);
    }

    public TransferFundMsg(String type) {
        super(type);
    }

    public TransferFundMsg(long fromAccountId, long toAccountId, String transCurrency, double amount, String date) {
        this();
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.transCurrency = transCurrency;
        this.amount = amount;
        this.date = date;
    }

    public long getFromAccountId() {
        return fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public String getTransCurrency() {
        return transCurrency;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
