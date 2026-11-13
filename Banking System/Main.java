public class Main {
    public static void main(String[] args) {
        // 1. Initialize DB (Setup tables)
        DatabaseConnection.initializeDatabase();

        // (Optional) Setup a test account in the DB so we have something to deposit into
        AccountDAO accDao = new AccountDAO();
        if (accDao.findAccountByNumber("SAV-999") == null) {
            SavingsAccount testAccount = new SavingsAccount("SAV-999", 1000.00, 0.0005);
            accDao.saveAccount(testAccount, 99);
        }

        // 2. Launch the GUI (Boundary Classes)
        LoginView loginView = new LoginView();
        loginView.displayLoginScreen(); // Show Login

        AccountView accountView = new AccountView();
        accountView.displayDepositScreen(); // Show Deposit screen
        
        System.out.println("\nThank you for using BAC Bank. Goodbye!");
    }
}