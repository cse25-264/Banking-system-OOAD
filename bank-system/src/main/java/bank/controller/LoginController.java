package bank.controller;

import bank.dao.CustomerDAO;
import bank.dao.DatabaseException;
import bank.model.Customer;
import bank.util.PasswordUtil;
import bank.view.Alerts;
import bank.view.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {
    private final Stage stage;
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final LoginView view = new LoginView();

    public LoginController(Stage stage) {
        this.stage = stage;
        view.getLoginButton().setOnAction(e -> handleLogin());
        view.getRegisterButton().setOnAction(e -> new RegisterController(stage).show());
    }

    public void show() {
        stage.setTitle("Banking System - Login");
        stage.setScene(new Scene(view.getRoot()));
        stage.sizeToScene();
        stage.show();
    }

    private void handleLogin() {
        String username = view.getUsername().trim();
        String password = view.getPassword();
        if (username.isEmpty() || password.isEmpty()) {
            Alerts.error("Enter your username and password.");
            return;
        }
        try {
            Customer customer = customerDAO.findByLogin(username, PasswordUtil.hash(password));
            if (customer == null) {
                Alerts.error("Invalid username or password.");
                view.clearPassword();
                return;
            }
            new AccountController(stage, customer).show();
        } catch (DatabaseException ex) {
            Alerts.error(ex.getMessage());
        }
    }
}
