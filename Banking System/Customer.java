import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int customerId;
    private String firstName;
    private String surname;
    private String address;
    private List<Account> accounts; // One customer can have many accounts

    public Customer(int customerId, String firstName, String surname, String address) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.accounts = new ArrayList<>();
    }

    public void openAccount(Account account) {
        this.accounts.add(account);
        System.out.println("Opened " + account.getClass().getSimpleName() + " for " + firstName);
    }

    public List<Account> getAccounts() {
        return accounts;
    }
    
    public String getName() {
        return firstName + " " + surname;
    }

    public int getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
}