package springboot.model;

import javax.persistence.*;

@Entity
public class FundRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private long accountId;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private double amount;
}