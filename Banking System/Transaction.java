import java.util.Date;

public class Transaction {
    private String transactionId;
    private double amount;
    private String type;
    private Date date;

    public Transaction(String transactionId, double amount, String type, Date date) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public String getDetails() {
        return "Txn ID: " + transactionId + " | Amount: P" + amount + " | Type: " + type + " | Date: " + date;
    }
}