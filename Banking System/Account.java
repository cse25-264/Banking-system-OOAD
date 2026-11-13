import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Account implements Transactable {
    // Encapsulation: protected means subclasses can access them, but outside classes cannot
    protected String accountNumber;
    protected double balance;
    protected List<Transaction> transactions;

    public Account(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            // Create a transaction record automatically on deposit
            Transaction txn = new Transaction("TXN" + System.currentTimeMillis(), amount, "Deposit", new Date());
            transactions.add(txn);
        }
    }

    // We don't write the body for withdraw() here. We force the subclasses to do it!
    @Override
    public abstract void withdraw(double amount);

    @Override
    public double getBalance() {
        return this.balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}