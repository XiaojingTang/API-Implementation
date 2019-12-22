package websocket.messages;

public class FundAccountMsg extends BaseMessage {
    public static final String TYPE = "FUND_ACCOUNT";

    private Long accountId;
    private String transCurrency;
    private Double amount;
    private String date;

    public FundAccountMsg() {
        super(TYPE);
    }

    public FundAccountMsg(Long accountId, String transCurrency, Double amount, String date) {
        this();
        this.accountId = accountId;
        this.transCurrency = transCurrency;
        this.amount = amount;
        this.date = date;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getTransCurrency() {
        return transCurrency;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
