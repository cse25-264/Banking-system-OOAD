package bank.model;

/** INTERFACE: anything that can receive and release money. */
public interface Transactable {
    void deposit(double amount);
    void withdraw(double amount);
    double getBalance();
}
