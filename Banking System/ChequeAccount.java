public class ChequeAccount extends Account {
    private double overdraftLimit;

    public ChequeAccount(String accountNumber, double initialBalance, double overdraftLimit) {
        super(accountNumber, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        // Allows withdrawal even if balance goes slightly negative (up to overdraft limit)
        if (amount <= (this.balance + this.overdraftLimit)) {
            this.balance -= amount;
        } else {
            System.out.println("Error: Overdraft limit exceeded.");
        }
    }
}