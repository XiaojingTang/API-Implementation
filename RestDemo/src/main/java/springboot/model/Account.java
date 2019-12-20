package springboot.model;

import javax.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String user;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private Double balance;

    public Account() {
    }

    public Account(Long id, String user, String currency) {
        this.id = id;
        this.user = user;
        this.currency = currency;
        this.balance = 0.0;
    }

    public Long getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}