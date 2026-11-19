package bank.model;
// to extend to my subclasses  Transcatable is the mother of my classes(account ones)
public interface Transactable {
    void deposit(double amount);
    void withdraw(double amount);
    double getBalance();
}
