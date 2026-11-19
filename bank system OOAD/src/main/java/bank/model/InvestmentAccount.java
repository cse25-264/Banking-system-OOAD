package bank.model;

import java.util.List;

public class InvestmentAccount extends Account implements Payable {
    public static final double MIN_OPENING_DEPOSIT = 500.00;
    private final double interestRate = 0.05;
//InvestmentAccount constructor with account number, branch, and opening deposit, which calls the parent class constructor and checks for minimum opening deposit.
    public InvestmentAccount(String accountNumber, String branch, double openingDeposit) {
        super(accountNumber, branch);
        if (openingDeposit < MIN_OPENING_DEPOSIT) {
            throw new IllegalArgumentException(
                "Investment account needs an opening deposit of at least BWP500.00.");
        }
        deposit(openingDeposit, "OPENING");
    }

    public InvestmentAccount(String accountNumber, String branch, double balance, List<Transaction> history) {
        super(accountNumber, branch, balance, history);//called the constructor of the parent class Account
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
