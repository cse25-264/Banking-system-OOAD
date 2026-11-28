package bank.dao;

/** Thrown when something goes wrong with the database. */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
