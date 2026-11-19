package bank.view;

import bank.model.Account;
import bank.model.Transaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class DashboardView {
    private final Label welcomeLabel = new Label();
    private final TableView<Account> accountTable = new TableView<>();
    private final TableView<Transaction> historyTable = new TableView<>();
    private final TextField amountField = new TextField();
    private final Button depositButton = new Button("Deposit");
    private final Button withdrawButton = new Button("Withdraw");
    private final Button interestButton = new Button("Apply monthly interest");
    private final Button openAccountButton = new Button("Open new account");
    private final Button logoutButton = new Button("Logout");
    private final BorderPane root = new BorderPane();

    public DashboardView() {
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(10, welcomeLabel, spacer, logoutButton);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(0, 0, 10, 0));

        buildAccountTable();
        buildHistoryTable();
        accountTable.setPrefHeight(180);
        historyTable.setPrefHeight(200);

        amountField.setPromptText("Amount (BWP)");
        HBox actions = new HBox(10, amountField, depositButton, withdrawButton,
                interestButton, openAccountButton);
        actions.setPadding(new Insets(10, 0, 10, 0));

        VBox center = new VBox(5, new Label("My accounts"), accountTable, actions,
                new Label("Transaction history (selected account)"), historyTable);

        root.setPadding(new Insets(15));
        root.setTop(top);
        root.setCenter(center);
        root.setPrefSize(820, 560);
    }

    private void buildAccountTable() {
        TableColumn<Account, String> number = new TableColumn<>("Account no.");
        number.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAccountNumber()));
        TableColumn<Account, String> type = new TableColumn<>("Type");
        type.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAccountType()));
        TableColumn<Account, String> branch = new TableColumn<>("Branch");
        branch.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBranch()));
        TableColumn<Account, String> balance = new TableColumn<>("Balance");
        balance.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("BWP %,.2f", c.getValue().getBalance())));
        accountTable.getColumns().addAll(number, type, branch, balance);
        accountTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void buildHistoryTable() {
        TableColumn<Transaction, String> id = new TableColumn<>("ID");
        id.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTransactionId()));
        TableColumn<Transaction, String> type = new TableColumn<>("Type");
        type.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getType()));
        TableColumn<Transaction, String> amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("BWP %,.2f", c.getValue().getAmount())));
        TableColumn<Transaction, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDate().toString()));
        historyTable.getColumns().addAll(id, type, amount, date);
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public Parent getRoot() { return root; }
    public void setWelcome(String text) { welcomeLabel.setText(text); }
    public void setAccounts(List<Account> accounts) {
        accountTable.setItems(FXCollections.observableArrayList(accounts));
    }
    public void setHistory(List<Transaction> transactions) {
        historyTable.setItems(FXCollections.observableArrayList(transactions));
    }
    public Account getSelectedAccount() { return accountTable.getSelectionModel().getSelectedItem(); }
    public void selectAccount(Account account) { accountTable.getSelectionModel().select(account); }
    public TableView<Account> getAccountTable() { return accountTable; }
    public String getAmountText() { return amountField.getText(); }
    public void clearAmount() { amountField.clear(); }

    public Button getDepositButton() { return depositButton; }
    public Button getWithdrawButton() { return withdrawButton; }
    public Button getInterestButton() { return interestButton; }
    public Button getOpenAccountButton() { return openAccountButton; }
    public Button getLogoutButton() { return logoutButton; }
}
