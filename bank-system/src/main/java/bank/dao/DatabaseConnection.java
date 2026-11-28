package bank.dao;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Opens connections to the SQLite database file and creates the tables. */
public class DatabaseConnection {
    private static String url = "jdbc:sqlite:bank.db";

    // Lets the integration test use a different database file
    public static void setUrl(String newUrl) { url = newUrl; }

    public static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(url);
        try (Statement st = con.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");   // makes SQLite enforce foreign keys
        }
        return con;
    }

    /** Reads schema.sql and runs every CREATE TABLE statement. */
    public static void initSchema() {
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/schema.sql")) {
            String script = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            try (Connection con = getConnection(); Statement st = con.createStatement()) {
                for (String sql : script.split(";")) {
                    if (!sql.isBlank()) st.execute(sql);
                }
            }
        } catch (Exception e) {
            throw new DatabaseException("Could not create the database tables", e);
        }
    }
}
