package websocket.objects;

public class Account {
    private long id;
    private String user;
    private String currency;
    private double balance;

    public Account() {
    }

    public Account(long id, String user, String currency, double balance) {
        this.id = id;
        this.user = user;
        this.currency = currency;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getCurrency() {
        return currency;
    }

    public double getBalance() {
        return balance;
    }
}