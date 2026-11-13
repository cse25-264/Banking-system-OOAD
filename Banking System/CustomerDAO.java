 import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

     public void saveCustomer(Customer customer) {
        String sql = "INSERT INTO customers (customer_id, first_name, surname, address) VALUES (?, ?, ?, ?)";
        
         try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, customer.getCustomerId());
            pstmt.setString(2, customer.getFirstName());
            pstmt.setString(3, customer.getSurname());
            pstmt.setString(4, customer.getAddress());
            
            pstmt.executeUpdate();
            System.out.println("Customer saved to database: " + customer.getFirstName());
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     public Customer findCustomerById(int id) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        Customer customer = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("surname"),
                        rs.getString("address")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
}