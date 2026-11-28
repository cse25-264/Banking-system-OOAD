package bank.model;

import java.util.List;

/** Salary account. Only for working people: employer details are required. */
public class ChequeAccount extends Account {
    private final String companyName;
    private final String companyAddress;
    private final double overdraftLimit;

    // NEW account (no overdraft)
    public ChequeAccount(String accountNumber, String branch, String companyName, String companyAddress) {
        this(accountNumber, branch, companyName, companyAddress, 0.0);
    }

    // NEW account with an overdraft limit
    public ChequeAccount(String accountNumber, String branch, String companyName,
                         String companyAddress, double overdraftLimit) {
        super(accountNumber, branch);
        if (companyName == null || companyName.isBlank()
                || companyAddress == null || companyAddress.isBlank()) {
            throw new IllegalArgumentException(
                "Cheque account is only for working people (company name and address required).");
        }
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.overdraftLimit = overdraftLimit;
    }

    // used when loading from the database
    public ChequeAccount(String accountNumber, String branch, String companyName, String companyAddress,
                         double overdraftLimit, double balance, List<Transaction> history) {
        super(accountNumber, branch, balance, history);
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.overdraftLimit = overdraftLimit;
    }

    public String getCompanyName() { return companyName; }
    public String getCompanyAddress() { return companyAddress; }
    public double getOverdraftLimit() { return overdraftLimit; }

    @Override
    protected double getAvailableFunds() { return getBalance() + overdraftLimit; }

    @Override
    public String getAccountType() { return "Cheque"; }
}
