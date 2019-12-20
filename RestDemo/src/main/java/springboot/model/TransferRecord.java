package springboot.model;

import javax.persistence.*;

@Entity
public class TransferRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private long fromAccountId;

    @Column(nullable = false)
    private long toAccountId;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private double amount;
}