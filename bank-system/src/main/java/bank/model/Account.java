package bank.model;

import java.util.ArrayList;
import java.util.List;

/** ABSTRACT class: the common parts of every account (abstraction + encapsulation). */
public abstract class Account implements Transactable {
    public static final String DEFAULT_BRANCH = "Main Mall";

    private final String accountNumber;
    private final String branch;
    private double balance;
    private final List<Transaction> transactions = new ArrayList<>();

    // Constructor for a NEW account
    protected Account(String accountNumber, String branch) {
        this.accountNumber = accountNumber;
        this.branch = branch;
    }

    // Constructor used when an account is LOADED from the database
    protected Account(String accountNumber, String branch, double balance, List<Transaction> history) {
        this(accountNumber, branch);
        this.balance = balance;
        this.transactions.addAll(history);
    }

    public String getAccountNumber() { return accountNumber; }
    public String getBranch() { return branch; }

    @Override
    public double getBalance() { return balance; }

    public List<Transaction> getTransactions() { return new ArrayList<>(transactions); }

    public Transaction getLastTransaction() {
        return transactions.isEmpty() ? null : transactions.get(transactions.size() - 1);
    }

    // method ooveriding
    @Override
    public void deposit(double amount) {
        deposit(amount, "DEPOSIT");
    }

    public void deposit(double amount, String type) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        balance += amount;
        transactions.add(new Transaction(amount, type));
    }

    // Subclasses overide this when their rules are different
    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        if (amount > getAvailableFunds()) {
            throw new IllegalStateException("Insufficient funds.");
        }
        balance -= amount;
        transactions.add(new Transaction(amount, "WITHDRAWAL"));
    }

    protected double getAvailableFunds() { return balance; }

    public abstract String getAccountType();

    @Override
    public String toString() {
        return String.format("%s [%s] balance: BWP %,.2f", accountNumber, getAccountType(), balance);
    }
}
