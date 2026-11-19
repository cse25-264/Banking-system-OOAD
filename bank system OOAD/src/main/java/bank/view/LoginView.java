package bank.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginView {
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button loginButton = new Button("Login");
    private final Button registerButton = new Button("Register new customer");
    private final VBox root = new VBox(12);

    public LoginView() {
        Label title = new Label("Banking System");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");
        loginButton.setDefaultButton(true);
        loginButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setMaxWidth(Double.MAX_VALUE);

        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setPrefWidth(340);
        root.getChildren().addAll(title, usernameField, passwordField, loginButton, registerButton);
    }

    public Parent getRoot() { return root; }
    public String getUsername() { return usernameField.getText(); }
    public String getPassword() { return passwordField.getText(); }
    public void clearPassword() { passwordField.clear(); }
    public Button getLoginButton() { return loginButton; }
    public Button getRegisterButton() { return registerButton; }
}
