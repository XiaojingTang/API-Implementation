package websocket.objects;

public class FundRecord {
    private long id;
    private long accountId;
    private String transCurrency;
    private double amount;
    private String date;
    private double conversionRate;
    private double crdhldBillAmt;
    private long bankFee;

    public FundRecord() {
    }

    public FundRecord(long id, long accountId, String transCurrency, double amount, String date, double conversionRate,
                      double crdhldBillAmt, long bankFee) {
        this.id = id;
        this.accountId = accountId;
        this.transCurrency = transCurrency;
        this.amount = amount;
        this.date = date;
        this.conversionRate = conversionRate;
        this.crdhldBillAmt = crdhldBillAmt;
        this.bankFee = bankFee;
    }

    public long getId() {
        return id;
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

    public double getConversionRate() {
        return conversionRate;
    }

    public double getCrdhldBillAmt() {
        return crdhldBillAmt;
    }

    public long getBankFee() {
        return bankFee;
    }
}