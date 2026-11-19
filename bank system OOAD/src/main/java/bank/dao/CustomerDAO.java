package bank.dao;

import bank.model.Account;
import bank.model.Customer;

import java.sql.*;

public class CustomerDAO {
    private final AccountDAO accountDAO = new AccountDAO();

    public int nextCustomerId() {
        String sql = "SELECT COALESCE(MAX(customer_id), 0) + 1 FROM customer";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new DatabaseException("Could not get the next customer id", e);
        }
    }
//inserts a new customer into the database, including their details and login credentials, using a prepared statement to prevent SQL injection.
    public void insert(Customer c, String username, String passwordHash) {
        String sql = "INSERT INTO customer (customer_id, first_name, surname, address, username, password) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getCustomerId());
            ps.setString(2, c.getFirstName());
            ps.setString(3, c.getSurname());
            ps.setString(4, c.getAddress());
            ps.setString(5, username);
            ps.setString(6, passwordHash);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not save the customer", e);
        }
    }
//checks if a given username already exists in the database by executing a SELECT query and returning true if a matching record is found, indicating that the username is taken.
    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM customer WHERE username = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not check the username", e);
        }
    }
//retrieves a customer from the database based on their login credentials (username and password hash), returning a Customer object if a match is found, or null if no matching record exists.
    public Customer findByLogin(String username, String passwordHash) {
        Customer customer = null;
        String sql = "SELECT * FROM customer WHERE username = ? AND password = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer(rs.getInt("customer_id"), rs.getString("first_name"),
                                            rs.getString("surname"), rs.getString("address"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not read the customer", e);
        }
        if (customer != null) {
            for (Account a : accountDAO.findByCustomerId(customer.getCustomerId())) {
                customer.openAccount(a);
            }
        }
        return customer;
    }
//updates the details of an existing customer in the database, including their first name, surname, and address, using a prepared statement to ensure safe execution of the SQL UPDATE command.
    public void update(Customer c) {
        String sql = "UPDATE customer SET first_name = ?, surname = ?, address = ? WHERE customer_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getFirstName());
            ps.setString(2, c.getSurname());
            ps.setString(3, c.getAddress());
            ps.setInt(4, c.getCustomerId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not update the customer", e);
        }
    }

    public void delete(int customerId) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete the customer", e);
        }
    }
}
