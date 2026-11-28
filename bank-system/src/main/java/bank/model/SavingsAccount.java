package bank.model;

import java.util.List;

/** INHERITANCE: pays 0.05% monthly interest. No withdrawals allowed. */
public class SavingsAccount extends Account implements Payable {
    private final double interestRate = 0.0005; // 0.05%

    public SavingsAccount(String accountNumber, String branch) {
        super(accountNumber, branch);
    }

    // used when loading from the database
    public SavingsAccount(String accountNumber, String branch, double balance, List<Transaction> history) {
        super(accountNumber, branch, balance, history);
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
