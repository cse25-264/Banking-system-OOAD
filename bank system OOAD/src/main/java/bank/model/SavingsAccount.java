package bank.model;

import java.util.List;

public class SavingsAccount extends Account implements Payable {
    private final double interestRate = 0.0005;

    public SavingsAccount(String accountNumber, String branch) {
        super(accountNumber, branch);
    }

    public SavingsAccount(String accountNumber, String branch, double balance, List<Transaction> history) {
        super(accountNumber, branch, balance, history);//called the constructor of the parent class Account
    }

    public double getInterestRate() { return interestRate; }

    @Override
    public void withdraw(double amount) {
        throw new UnsupportedOperationException("Savings accounts do not allow withdrawals.");
    }

    @Override
    public void addInterest() {
        double interest = getBalance() * interestRate;
        if (interest > 0) deposit(interest, "INTEREST");
    }

    @Override
    public String getAccountType() { return "Savings"; }
}
