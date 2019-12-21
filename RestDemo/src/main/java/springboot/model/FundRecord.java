package springboot.model;

import javax.persistence.*;

@Entity
public class FundRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long accountId;

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

    public FundRecord() {
    }

    public FundRecord(Long id, Long accountId, String transCurrency, Double amount, String date, Double conversionRate,
                      Double crdhldBillAmt, Long bankFee) {
        this.id = id;
        this.accountId = accountId;
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