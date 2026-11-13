import javafx.application.Application;
import javafx.stage.Stage;

public class BankApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        DatabaseConnection.initializeDatabase();
        
        // Setup a test account if it doesn't exist
        CustomerDAO custDao = new CustomerDAO();
        if (custDao.findCustomerById(99) == null) {
            custDao.saveCustomer(new Customer(99, "Neo", "Molefe", "Gaborone West"));
        }
        AccountDAO accDao = new AccountDAO();
        if (accDao.findAccountByNumber("SAV-999") == null) {
            accDao.saveAccount(new SavingsAccount("SAV-999", 1000.00, 0.0005), 99);
        }

        // Launch LoginView
        LoginView loginView = new LoginView(primaryStage);
        loginView.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}