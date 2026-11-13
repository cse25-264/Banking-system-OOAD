import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AccountView {
    private Stage stage;
    private AccountController controller;

    public AccountView(Stage stage) {
        this.stage = stage;
        this.controller = new AccountController(); // Connects to your Controller!
    }

    public void show() {
        // Create UI components
        Label title = new Label("Deposit Funds");
        Label accLabel = new Label("Account Number:");
        TextField accField = new TextField();
        Label amtLabel = new Label("Amount (P):");
        TextField amtField = new TextField();
        Button depositBtn = new Button("Deposit");
        Label statusLabel = new Label();

        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(title, 0, 0, 2, 1);
        grid.add(accLabel, 0, 1);
        grid.add(accField, 1, 1);
        grid.add(amtLabel, 0, 2);
        grid.add(amtField, 1, 2);
        grid.add(depositBtn, 1, 3);
        grid.add(statusLabel, 0, 4, 2, 1);

        // Button Logic
        depositBtn.setOnAction(e -> {
            String accNum = accField.getText();
            try {
                double amount = Double.parseDouble(amtField.getText());
                
                // Call the Controller (Business Logic)
                boolean success = controller.deposit(accNum, amount);
                
                if (success) {
                    statusLabel.setText("Deposit Successful!");
                    statusLabel.setStyle("-fx-text-fill: green;");
                } else {
                    statusLabel.setText("Account not found.");
                    statusLabel.setStyle("-fx-text-fill: red;");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid amount entered.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        Scene scene = new Scene(grid, 350, 300);
        stage.setTitle("Account Dashboard");
        stage.setScene(scene);
    }
}