package bank.controller;

import bank.dao.CustomerDAO;
import bank.dao.DatabaseException;
import bank.model.Customer;
import bank.util.PasswordUtil;
import bank.view.Alerts;
import bank.view.RegisterView;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegisterController {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final RegisterView view = new RegisterView();
    private final Stage dialog = new Stage();

    public RegisterController(Stage owner) {
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Register customer");
        dialog.setScene(new Scene(view.getRoot()));
        view.getRegisterButton().setOnAction(e -> handleRegister());
        view.getCancelButton().setOnAction(e -> dialog.close());
    }

    public void show() { dialog.showAndWait(); }

    private void handleRegister() {
        try {
            String first = view.getFirstName().trim();
            String surname = view.getSurname().trim();
            String address = view.getAddress().trim();
            String username = view.getUsername().trim();
            String password = view.getPassword();
            if (first.isEmpty() || surname.isEmpty() || address.isEmpty()
                    || username.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException("All fields are required.");
            }
            if (password.length() < 4) {
                throw new IllegalArgumentException("Password must be at least 4 characters.");
            }
            if (customerDAO.usernameExists(username)) {
                throw new IllegalArgumentException("That username is already taken.");
            }
            Customer customer = new Customer(customerDAO.nextCustomerId(), first, surname, address);
            customerDAO.insert(customer, username, PasswordUtil.hash(password));

            Alerts.info("Registration successful. You can now log in.");
            dialog.close();
        } catch (IllegalArgumentException | DatabaseException ex) {
            Alerts.error(ex.getMessage());
        }
    }
}
