package bank.dao;

import bank.model.*;
import bank.util.PasswordUtil;

public class SampleData {
    public static void insertIfEmpty() {
        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();

        if (customerDAO.usernameExists("jay")) return;

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

            Account savings = new SavingsAccount(accountDAO.nextAccountNumber(), Account.DEFAULT_BRANCH);
            savings.deposit(1000 + i * 250);
            accountDAO.insert(savings, c.getCustomerId());

            if (i % 2 == 0) {
                Account invest = new InvestmentAccount(accountDAO.nextAccountNumber(),
                                                       Account.DEFAULT_BRANCH, 500 + i * 100);
                accountDAO.insert(invest, c.getCustomerId());
            } else {
                Account cheque = new ChequeAccount(accountDAO.nextAccountNumber(), Account.DEFAULT_BRANCH,
                                                   "Debswana", "Orapa House, Gaborone");
                cheque.deposit(5000);
                accountDAO.insert(cheque, c.getCustomerId());
            }
        }
    }
}
