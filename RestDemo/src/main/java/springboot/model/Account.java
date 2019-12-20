package springboot.model;

//import javax.persistence.*;

import javax.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String user;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private double balance;
}