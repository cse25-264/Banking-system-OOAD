import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    public void saveAccount(Account account, int customerId) {
        String sql = "INSERT INTO accounts (account_number, customer_id, balance, account_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setInt(2, customerId);
            pstmt.setDouble(3, account.getBalance());
            if (account instanceof SavingsAccount) {
                pstmt.setString(4, "Savings");
            } else if (account instanceof InvestmentAccount) {
                pstmt.setString(4, "Investment");
            } else if (account instanceof ChequeAccount) {
                pstmt.setString(4, "Cheque");
            }
            pstmt.executeUpdate();
            System.out.println("Account saved to DB: " + account.getAccountNumber());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account findAccountByNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        Account account = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("account_type");
                    double balance = rs.getDouble("balance");
                    if (type.equals("Savings")) {
                        account = new SavingsAccount(accountNumber, balance, 0.0005);
                    } else if (type.equals("Investment")) {
                        account = new InvestmentAccount(accountNumber, balance, 0.05);
                    } else {
                        account = new ChequeAccount(accountNumber, balance, 500.00);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
    
    public void updateAccountBalance(Account account) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, account.getBalance());
            pstmt.setString(2, account.getAccountNumber());
            pstmt.executeUpdate();
            System.out.println("Account balance updated in DB: " + account.getAccountNumber());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}