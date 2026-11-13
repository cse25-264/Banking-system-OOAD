import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginView {
    private Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // Create UI components
        Label title = new Label("BAC Banking System - Login");
        Label idLabel = new Label("Customer ID:");
        TextField idField = new TextField();
        Button loginBtn = new Button("Login");

        // Layout (GridPane)
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(title, 0, 0, 2, 1);
        grid.add(idLabel, 0, 1);
        grid.add(idField, 1, 1);
        grid.add(loginBtn, 1, 2);

        // Button Logic (Event Handler)
        loginBtn.setOnAction(e -> {
            String customerId = idField.getText();
            if (!customerId.isEmpty()) {
                System.out.println("Login attempt for ID: " + customerId);
                // Switch to Account View
                AccountView accountView = new AccountView(stage);
                accountView.show();
            } else {
                System.out.println("Please enter a Customer ID.");
            }
        });

        Scene scene = new Scene(grid, 350, 250);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}