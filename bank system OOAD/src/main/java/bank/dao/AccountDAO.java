package bank.dao;

import bank.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    private final TransactionDAO transactionDAO = new TransactionDAO();
//assigns the next account number by querying the database for the maximum account number and incrementing it by 1, returning it as a string with the prefix "ACC".
    public String nextAccountNumber() {
        String sql = "SELECT COALESCE(MAX(CAST(SUBSTR(account_number, 4) AS INTEGER)), 1000) + 1 FROM account";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return "ACC" + rs.getInt(1);
        } catch (SQLException e) {
            throw new DatabaseException("Could not get the next account number", e);
        }
    }
//inserts a new account into the database, including its details and associated transactions, using a prepared statement to prevent SQL injection.
    public void insert(Account a, int customerId) {
        String sql = "INSERT INTO account (account_number, customer_id, account_type, branch, balance, "
                   + "company_name, company_address, overdraft_limit) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getAccountNumber());
            ps.setInt(2, customerId);
            ps.setString(3, a.getAccountType());
            ps.setString(4, a.getBranch());
            ps.setDouble(5, a.getBalance());
            if (a instanceof ChequeAccount cheque) {
                ps.setString(6, cheque.getCompanyName());
                ps.setString(7, cheque.getCompanyAddress());
                ps.setDouble(8, cheque.getOverdraftLimit());
            } else {
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
                ps.setDouble(8, 0);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not save the account", e);
        }
        for (Transaction t : a.getTransactions()) {
            transactionDAO.insert(t, a.getAccountNumber());
        }
    }
//retrieves a list of accounts associated with a specific customer ID from the database, constructing Account objects based on the account type and returning them in a list.
    public List<Account> findByCustomerId(int customerId) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE customer_id = ? ORDER BY account_number";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(buildAccount(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not read accounts", e);
        }
        return list;
    }
//builds an Account object based on the data retrieved from the ResultSet, determining the account type and creating the appropriate subclass instance (SavingsAccount, InvestmentAccount, or ChequeAccount) with its associated transaction history.
    private Account buildAccount(ResultSet rs) throws SQLException {
        String number = rs.getString("account_number");
        String type = rs.getString("account_type");
        String branch = rs.getString("branch");
        double balance = rs.getDouble("balance");
        String company = rs.getString("company_name");
        String companyAddress = rs.getString("company_address");
        double overdraft = rs.getDouble("overdraft_limit");
        List<Transaction> history = transactionDAO.findByAccount(number);

        return switch (type) {
            case "Savings" -> new SavingsAccount(number, branch, balance, history);
            case "Investment" -> new InvestmentAccount(number, branch, balance, history);
            case "Cheque" -> new ChequeAccount(number, branch, company, companyAddress,
                                               overdraft, balance, history);
            default -> throw new IllegalStateException("Unknown account type: " + type);
        };
    }
//updates the balance of an existing account in the database by executing an SQL UPDATE statement with the new balance and account number, ensuring that the account's balance is kept in sync with the database.
    public void updateBalance(Account a) {
        String sql = "UPDATE account SET balance = ? WHERE account_number = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, a.getBalance());
            ps.setString(2, a.getAccountNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not update the balance", e);
        }
    }
//deletes an account from the database based on the provided account number, executing an SQL DELETE statement to remove the account record.
    public void delete(String accountNumber) {
        String sql = "DELETE FROM account WHERE account_number = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete the account", e);
        }
    }
}
