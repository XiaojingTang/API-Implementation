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
    private String currency;

    @Column(nullable = false)
    private Double amount;

    public FundRecord() {
    }

    public FundRecord(Long id, Long accountId, String currency, Double amount) {
        this.id = id;
        this.accountId = accountId;
        this.currency = currency;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getAmount() {
        return amount;
    }
}