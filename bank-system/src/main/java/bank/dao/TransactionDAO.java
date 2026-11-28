package bank.dao;

import bank.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Data access for the transactions table (a bank ledger: create and read only)
public class TransactionDAO {

    // CREATE
    public void insert(Transaction t, String accountNumber) {
        String sql = "INSERT INTO transactions (transaction_id, account_number, amount, type, tx_date) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getTransactionId());
            ps.setString(2, accountNumber);
            ps.setDouble(3, t.getAmount());
            ps.setString(4, t.getType());
            ps.setLong(5, t.getDate().getTime());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not save the transaction", e);
        }
    }

    // READ
    public List<Transaction> findByAccount(String accountNumber) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY tx_date, rowid";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Transaction(
                            rs.getString("transaction_id"),
                            rs.getDouble("amount"),
                            rs.getString("type"),
                            new Date(rs.getLong("tx_date"))));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not read transactions", e);
        }
        return list;
    }
}
