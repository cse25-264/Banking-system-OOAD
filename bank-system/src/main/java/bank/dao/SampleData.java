package bank.dao;

import bank.model.*;
import bank.util.PasswordUtil;

/** Inserts 10 sample customers (with accounts) the first time the app runs. */
public class SampleData {

    public static void insertIfEmpty() {
        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();

        if (customerDAO.usernameExists("jay")) return;   // already inserted

        String[][] people = {
            {"Jay", "Molefe", "Plot 123, Gaborone"},
            {"Kabelo", "Mokoena", "Block 8, Francistown"},
            {"Neo", "Modise", "Phakalane, Gaborone"},
            {"Tshepo", "Kgosi", "Mochudi"},
            {"Lesedi", "Sebego", "Maun"},
            {"Thato", "Ramotswe", "Lobatse"},
            {"Boitumelo", "Seretse", "Serowe"},
            {"Masego", "Tau", "Palapye"},
            {"Onkabetse", "Phiri", "Kanye"},
            {"Keitumetse", "Moeng", "Molepolole"}
        };

        for (int i = 0; i < people.length; i++) {
            Customer c = new Customer(customerDAO.nextCustomerId(),
                                      people[i][0], people[i][1], people[i][2]);
            String username = people[i][0].toLowerCase();
            customerDAO.insert(c, username, PasswordUtil.hash("1234"));

            // Everyone gets a savings account
            Account savings = new SavingsAccount(accountDAO.nextAccountNumber(), Account.DEFAULT_BRANCH);
            savings.deposit(1000 + i * 250);
            accountDAO.insert(savings, c.getCustomerId());

            if (i % 2 == 0) {
                // Even rows also get an investment account
                Account invest = new InvestmentAccount(accountDAO.nextAccountNumber(),
                                                       Account.DEFAULT_BRANCH, 500 + i * 100);
                accountDAO.insert(invest, c.getCustomerId());
            } else {
                // Odd rows also get a cheque account
                Account cheque = new ChequeAccount(accountDAO.nextAccountNumber(), Account.DEFAULT_BRANCH,
                                                   "Debswana", "Orapa House, Gaborone");
                cheque.deposit(5000);
                accountDAO.insert(cheque, c.getCustomerId());
            }
        }
    }
}
