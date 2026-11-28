package bank.model;

import java.util.List;

/** Pays 5% monthly interest. Must be opened with at least BWP500. */
public class InvestmentAccount extends Account implements Payable {
    public static final double MIN_OPENING_DEPOSIT = 500.00;
    private final double interestRate = 0.05; // 5%

    // NEW account: the opening deposit rule is checked here
    public InvestmentAccount(String accountNumber, String branch, double openingDeposit) {
        super(accountNumber, branch);
        if (openingDeposit < MIN_OPENING_DEPOSIT) {
            throw new IllegalArgumentException(
                "Investment account needs an opening deposit of at least BWP500.00.");
        }
        deposit(openingDeposit, "OPENING");
    }

    // used when loading from the database
    public InvestmentAccount(String accountNumber, String branch, double balance, List<Transaction> history) {
        super(accountNumber, branch, balance, history);
    }

    public double getInterestRate() { return interestRate; }

    @Override
    public void addInterest() {
        double interest = getBalance() * interestRate;
        if (interest > 0) deposit(interest, "INTEREST");
    }

    @Override
    public String getAccountType() { return "Investment"; }
}
