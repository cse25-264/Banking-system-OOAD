import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // This creates a file-based database named bank_db in your project folder
    private static final String URL = "jdbc:h2:./bank_db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public static void initializeDatabase() {
        String createCustomerTable = "CREATE TABLE IF NOT EXISTS customers (" +
                "customer_id INT PRIMARY KEY, " +
                "first_name VARCHAR(50), " +
                "surname VARCHAR(50), " +
                "address VARCHAR(100))";

         try (Connection conn = getConnection(); 
             Statement stmt = conn.createStatement()) {
             
            stmt.execute(createCustomerTable);
            System.out.println("Database initialized successfully.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
