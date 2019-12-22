package springboot.model;

import javax.persistence.*;

@Entity
public class TransferRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long fromAccountId;

    @Column(nullable = false)
    private Long toAccountId;

    @Column(nullable = false)
    private String transCurrency;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private Double conversionRate;

    @Column(nullable = false)
    private Double crdhldBillAmt;

    @Column(nullable = false)
    private Long bankFee;

    public TransferRecord() {
    }

    public TransferRecord(Long id, Long fromAccountId, Long toAccountId, String transCurrency, Double amount, String date,
                          Double conversionRate, Double crdhldBillAmt, Long bankFee) {
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

    public Long getId() {
        return id;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public Long getToAccountId() {
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

    public Double getConversionRate() {
        return conversionRate;
    }

    public Double getCrdhldBillAmt() {
        return crdhldBillAmt;
    }

    public Long getBankFee() {
        return bankFee;
    }
}