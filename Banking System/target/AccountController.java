public class AccountController {
    
    private AccountDAO accountDAO;

    public AccountController() {
        this.accountDAO = new AccountDAO();
    }

     
    public boolean deposit(String accountNumber, double amount) {
         Account account = accountDAO.findAccountByNumber(accountNumber);
        
        if (account != null) {
             account.deposit(amount);
            
             accountDAO.updateAccountBalance(account);
            
            return true; // Success
        } else {
            System.out.println("Error: Account not found.");
            return false; // Failure
        }
    }
}