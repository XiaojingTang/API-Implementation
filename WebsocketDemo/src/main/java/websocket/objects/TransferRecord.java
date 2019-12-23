package websocket.objects;


public class TransferRecord {
    private long id;
    private long fromAccountId;
    private long toAccountId;
    private String transCurrency;
    private double amount;
    private String date;
    private double conversionRate;
    private double crdhldBillAmt;
    private long bankFee;

    public TransferRecord() {
    }

    public TransferRecord(long id, long fromAccountId, long toAccountId, String transCurrency, double amount, String date,
                          double conversionRate, double crdhldBillAmt, long bankFee) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
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