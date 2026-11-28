package bank.model;

import java.util.Date;
import java.util.UUID;

/** One record of money going in or out of an account. */
public class Transaction {
    private final String transactionId;
    private final double amount;
    private final String type;
    private final Date date;

    // Constructor for a NEW transaction (id and date are created automatically)
    public Transaction(double amount, String type) {
        this(UUID.randomUUID().toString().substring(0, 8).toUpperCase(), amount, type, new Date());
    }

    // Constructor used when a transaction is LOADED from the database
    public Transaction(String transactionId, double amount, String type, Date date) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public String getTransactionId() { return transactionId; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public Date getDate() { return date; }

    public String getDetails() {
        return String.format("%s | %-10s | BWP %,.2f | %s", transactionId, type, amount, date);
    }
}
