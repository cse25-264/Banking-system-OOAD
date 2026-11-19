package bank.controller;

import bank.dao.AccountDAO;
import bank.dao.DatabaseException;
import bank.dao.TransactionDAO;
import bank.model.*;
import bank.view.Alerts;
import bank.view.DashboardView;
import bank.view.OpenAccountView;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class AccountController {
    private final Stage stage;
    private final Customer customer;
    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final DashboardView view = new DashboardView();

    public AccountController(Stage stage, Customer customer) {
        this.stage = stage;
        this.customer = customer;

        view.getDepositButton().setOnAction(e -> safely(this::handleDeposit));
        view.getWithdrawButton().setOnAction(e -> safely(this::handleWithdraw));
        view.getInterestButton().setOnAction(e -> safely(this::handleInterest));
        view.getOpenAccountButton().setOnAction(e -> handleOpenAccount());
        view.getLogoutButton().setOnAction(e -> new LoginController(stage).show());
        view.getAccountTable().getSelectionModel().selectedItemProperty()
                .addListener((obs, old, acc) -> showHistory(acc));
    }

    public void show() {
        stage.setTitle("Banking System - Dashboard");
        stage.setScene(new Scene(view.getRoot()));
        stage.sizeToScene();
        view.setWelcome("Welcome, " + customer.getFirstName() + " " + customer.getSurname());
        refresh(null);
        stage.show();
    }

    private void handleDeposit() {
        Account account = requireSelected();
        account.deposit(parseAmount(view.getAmountText()));
        saveChange(account);
        view.clearAmount();
        refresh(account);
    }

    private void handleWithdraw() {
        Account account = requireSelected();
        account.withdraw(parseAmount(view.getAmountText()));
        saveChange(account);
        view.clearAmount();
        refresh(account);
    }

    private void handleInterest() {
        for (Account a : customer.getAccounts()) {
            if (a instanceof Payable p) {
                int before = a.getTransactions().size();
                p.addInterest();
                if (a.getTransactions().size() > before) saveChange(a);
            }
        }
        refresh(view.getSelectedAccount());
        Alerts.info("Monthly interest paid to all interest-bearing accounts.");
    }

    private void handleOpenAccount() {
        OpenAccountView openView = new OpenAccountView();
        Stage dialog = new Stage();
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Open account");
        dialog.setScene(new Scene(openView.getRoot()));
        openView.getCancelButton().setOnAction(e -> dialog.close());
        openView.getOpenButton().setOnAction(e -> safely(() -> {
            Account account = createAccount(openView);
            accountDAO.insert(account, customer.getCustomerId());
            customer.openAccount(account);
            refresh(account);
            dialog.close();
        }));
        dialog.showAndWait();
    }

    private Account createAccount(OpenAccountView openView) {
        String type = openView.getSelectedType();
        if (type == null) throw new IllegalArgumentException("Choose an account type.");
        String number = accountDAO.nextAccountNumber();
        return switch (type) {
            case "Savings" -> new SavingsAccount(number, Account.DEFAULT_BRANCH);
            case "Investment" -> new InvestmentAccount(number, Account.DEFAULT_BRANCH,
                    parseAmount(openView.getOpeningDepositText()));
            case "Cheque" -> new ChequeAccount(number, Account.DEFAULT_BRANCH,
                    openView.getCompanyName(), openView.getCompanyAddress());
            default -> throw new IllegalArgumentException("Unknown account type.");
        };
    }

    private void saveChange(Account account) {
        accountDAO.updateBalance(account);
        transactionDAO.insert(account.getLastTransaction(), account.getAccountNumber());
    }

    private void refresh(Account toSelect) {
        view.setAccounts(customer.getAccounts());
        if (toSelect != null) view.selectAccount(toSelect);
        showHistory(view.getSelectedAccount());
    }

    private void showHistory(Account account) {
        view.setHistory(account == null ? List.of() : account.getTransactions());
    }

    private Account requireSelected() {
        Account account = view.getSelectedAccount();
        if (account == null) throw new IllegalArgumentException("Select an account first.");
        return account;
    }

    private double parseAmount(String text) {
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException | NullPointerException ex) {
            throw new IllegalArgumentException("Enter a valid amount.");
        }
    }

    private void safely(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException | IllegalStateException
                 | UnsupportedOperationException | DatabaseException ex) {
            Alerts.error(ex.getMessage());
        }
    }
}
