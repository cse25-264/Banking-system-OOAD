package bank.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/** Boundary class: customer registration form. */
public class RegisterView {
    private final TextField firstNameField = new TextField();
    private final TextField surnameField = new TextField();
    private final TextField addressField = new TextField();
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button registerButton = new Button("Register");
    private final Button cancelButton = new Button("Cancel");
    private final GridPane root = new GridPane();

    public RegisterView() {
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(20));
        root.addRow(0, new Label("First name"), firstNameField);
        root.addRow(1, new Label("Surname"), surnameField);
        root.addRow(2, new Label("Address"), addressField);
        root.addRow(3, new Label("Username"), usernameField);
        root.addRow(4, new Label("Password"), passwordField);
        root.add(new HBox(10, registerButton, cancelButton), 1, 5);
    }

    public Parent getRoot() { return root; }
    public String getFirstName() { return firstNameField.getText(); }
    public String getSurname() { return surnameField.getText(); }
    public String getAddress() { return addressField.getText(); }
    public String getUsername() { return usernameField.getText(); }
    public String getPassword() { return passwordField.getText(); }
    public Button getRegisterButton() { return registerButton; }
    public Button getCancelButton() { return cancelButton; }
}
