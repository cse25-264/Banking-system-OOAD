package bank;

import bank.dao.*;
import bank.model.*;
import bank.util.PasswordUtil;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Integration test: checks that the model, the DAOs (JDBC) and the database
 * work together. It uses its own file (test-bank.db), so your real data is safe.
 * Run this class with the "Run" button in VS Code.
 */
public class IntegrationTest {
    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) throws Exception {
        Files.deleteIfExists(Path.of("test-bank.db"));
        DatabaseConnection.setUrl("jdbc:sqlite:test-bank.db");
        DatabaseConnection.initSchema();

        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();
        TransactionDAO transactionDAO = new TransactionDAO();
        String hash = PasswordUtil.hash("pass1234");

        System.out.println("=== BANKING SYSTEM INTEGRATION TEST ===");

        // ---- Test 1 and 2: register and login ----
        Customer c = new Customer(customerDAO.nextCustomerId(), "Test", "User", "Gaborone");
        customerDAO.insert(c, "testuser", hash);
        Customer login = customerDAO.findByLogin("testuser", hash);
        check("1. Register a customer and log in", login != null && login.getFirstName().equals("Test"));
        check("2. Login fails with a wrong password",
                customerDAO.findByLogin("testuser", PasswordUtil.hash("wrong")) == null);

        // ---- Test 3: open all three account types ----
        Account savings = new SavingsAccount(accountDAO.nextAccountNumber(), Account.DEFAULT_BRANCH);
        accountDAO.insert(savings, c.getCustomerId());
        Account invest = new InvestmentAccount(accountDAO.nextAccountNumber(), Account.DEFAULT_BRANCH, 1000);
        accountDAO.insert(invest, c.getCustomerId());
        Account cheque = new ChequeAccount(accountDAO.nextAccountNumber(), Account.DEFAULT_BRANCH,
                "Acme Ltd", "Plot 9, Gaborone");
        accountDAO.insert(cheque, c.getCustomerId());
        check("3. One customer holds 3 account types",
                customerDAO.findByLogin("testuser", hash).getAccounts().size() == 3);

        // ---- Test 4 to 6: deposits and withdrawals are saved ----
        savings.deposit(2000);
        save(accountDAO, transactionDAO, savings);
        invest.deposit(500);
        save(accountDAO, transactionDAO, invest);
        invest.withdraw(300);
        save(accountDAO, transactionDAO, invest);
        cheque.deposit(3000);
        save(accountDAO, transactionDAO, cheque);
        cheque.withdraw(500);
        save(accountDAO, transactionDAO, cheque);

        Customer reloaded = customerDAO.findByLogin("testuser", hash);
        check("4. Savings deposit is saved (2000.00)",
                near(find(reloaded, savings).getBalance(), 2000));
        check("5. Investment deposit and withdrawal are saved (1200.00)",
                near(find(reloaded, invest).getBalance(), 1200));
        check("6. Cheque deposit and withdrawal are saved (2500.00)",
                near(find(reloaded, cheque).getBalance(), 2500));

        // ---- Test 7 to 9: business rules ----
        boolean blocked = false;
        try { savings.withdraw(100); } catch (UnsupportedOperationException e) { blocked = true; }
        check("7. Savings account blocks withdrawals", blocked);

        blocked = false;
        try { new InvestmentAccount("ACC9999", "Main Mall", 200); }
        catch (IllegalArgumentException e) { blocked = true; }
        check("8. Investment account rejects an opening deposit below BWP500", blocked);

        blocked = false;
        try { new ChequeAccount("ACC9998", "Main Mall", "", ""); }
        catch (IllegalArgumentException e) { blocked = true; }
        check("9. Cheque account rejects a customer with no employer", blocked);

        // ---- Test 10: monthly interest ----
        for (Account a : reloaded.getAccounts()) {
            if (a instanceof Payable p) {
                p.addInterest();
                save(accountDAO, transactionDAO, a);
            }
        }
        Customer afterInterest = customerDAO.findByLogin("testuser", hash);
        check("10. Interest saved: Investment 5% (1260.00) and Savings 0.05% (2001.00)",
                near(find(afterInterest, invest).getBalance(), 1260)
                && near(find(afterInterest, savings).getBalance(), 2001));

        // ---- Test 11: transaction history ----
        check("11. Transaction history is saved (Investment has 4 records)",
                transactionDAO.findByAccount(invest.getAccountNumber()).size() == 4);

        // ---- Test 12: update ----
        c.setAddress("Francistown");
        customerDAO.update(c);
        check("12. Customer update is saved",
                customerDAO.findByLogin("testuser", hash).getAddress().equals("Francistown"));

        // ---- Test 13: delete ----
        accountDAO.delete(cheque.getAccountNumber());
        check("13. Deleting an account removes it and its transactions",
                customerDAO.findByLogin("testuser", hash).getAccounts().size() == 2
                && transactionDAO.findByAccount(cheque.getAccountNumber()).isEmpty());

        customerDAO.delete(c.getCustomerId());
        check("14. Deleting a customer removes the customer",
                customerDAO.findByLogin("testuser", hash) == null);

        // ---- Test 15: the 10 sample records ----
        DatabaseConnection.setUrl("jdbc:sqlite:test-bank.db");
        SampleData.insertIfEmpty();
        int found = 0;
        String[] names = {"jay", "kabelo", "neo", "tshepo", "lesedi",
                          "thato", "boitumelo", "masego", "onkabetse", "keitumetse"};
        for (String n : names) {
            if (customerDAO.findByLogin(n, PasswordUtil.hash("1234")) != null) found++;
        }
        check("15. Sample data: 10 customers can log in (found " + found + ")", found == 10);

        System.out.println("\nRESULT: " + passed + " passed, " + failed + " failed");
        Files.deleteIfExists(Path.of("test-bank.db"));
        if (failed > 0) System.exit(1);
    }

    // Saves the account's new balance and its newest transaction (same as the controller does)
    private static void save(AccountDAO a, TransactionDAO t, Account acc) {
        a.updateBalance(acc);
        t.insert(acc.getLastTransaction(), acc.getAccountNumber());
    }

    private static Account find(Customer c, Account original) {
        for (Account a : c.getAccounts()) {
            if (a.getAccountNumber().equals(original.getAccountNumber())) return a;
        }
        return null;
    }

    private static boolean near(double a, double b) { return Math.abs(a - b) < 0.001; }

    private static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS  " : "FAIL  ") + name);
        if (ok) passed++; else failed++;
    }
}
