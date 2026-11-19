package bank.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final int customerId;
    private String firstName;
    private String surname;
    private String address;
    private final List<Account> accounts = new ArrayList<>();

    public Customer(int customerId, String firstName, String surname, String address) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
    }

    public void openAccount(Account account) {
        if (account == null) throw new IllegalArgumentException("Account cannot be null.");
        accounts.add(account);
    }

    public List<Account> getAccounts() { return new ArrayList<>(accounts); }

    public int getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() { return customerId + " - " + firstName + " " + surname; }
}
