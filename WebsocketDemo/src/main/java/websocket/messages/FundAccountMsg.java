package websocket.messages;

public class FundAccountMsg extends BaseMessage {
    public static final String TYPE = "FUND_ACCOUNT";

    private long accountId;
    private String transCurrency;
    private double amount;
    private String date;

    public FundAccountMsg() {
        super(TYPE);
    }

    public FundAccountMsg(long accountId, String transCurrency, double amount, String date) {
        this();
        this.accountId = accountId;
        this.transCurrency = transCurrency;
        this.amount = amount;
        this.date = date;
    }

    public long getAccountId() {
        return accountId;
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
