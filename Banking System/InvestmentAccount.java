public class InvestmentAccount extends Account {
    private double interest;

    public InvestmentAccount(String accountNumber, double initialBalance, double interest) {
        super(accountNumber, initialBalance);
        
        // Assignment Rule: Minimum initial deposit of P500.00
        if (initialBalance < 500.00) {
            throw new IllegalArgumentException("Investment Account requires a minimum initial deposit of P500.00");
        }
        
        this.interest = interest;
    }

    @Override
    public void withdraw(double amount) {
        // Assignment rule: Withdrawals are allowed
        if (amount <= this.balance) {
            this.balance -= amount;
        } else {
            System.out.println("Error: Insufficient funds in Investment Account.");
        }
    }

    public void addInterest() {
        double interestAmount = this.balance * this.interest; // e.g., 0.05 for 5%
        deposit(interestAmount);
    }
}