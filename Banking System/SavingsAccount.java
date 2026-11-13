public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String accountNumber, double initialBalance, double interestRate) {
        super(accountNumber, initialBalance); // Calls the Account constructor
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount) {
        // Assignment rule: Savings accounts DO NOT allow withdrawals
        System.out.println("Error: Withdrawals are not allowed from a Savings Account.");
    }

    public void addInterest() {
        double interest = this.balance * this.interestRate; // e.g., 0.0005 for 0.05%
        deposit(interest);
    }
}