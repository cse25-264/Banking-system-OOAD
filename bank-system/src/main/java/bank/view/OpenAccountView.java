package bank.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Boundary class: form for opening a new account. */
public class OpenAccountView {
    private final ChoiceBox<String> typeBox =
            new ChoiceBox<>(FXCollections.observableArrayList("Savings", "Investment", "Cheque"));
    private final TextField openingDepositField = new TextField();
    private final TextField companyNameField = new TextField();
    private final TextField companyAddressField = new TextField();
    private final Button openButton = new Button("Open account");
    private final Button cancelButton = new Button("Cancel");
    private final VBox investmentBox = new VBox(5, new Label("Opening deposit (min BWP500)"), openingDepositField);
    private final VBox chequeBox = new VBox(5,
            new Label("Company name"), companyNameField,
            new Label("Company address"), companyAddressField);
    private final VBox root = new VBox(10);

    public OpenAccountView() {
        setVisibleAndManaged(investmentBox, false);
        setVisibleAndManaged(chequeBox, false);
        // Only changes which fields are shown (presentation)
        typeBox.getSelectionModel().selectedItemProperty().addListener((obs, old, type) -> {
            setVisibleAndManaged(investmentBox, "Investment".equals(type));
            setVisibleAndManaged(chequeBox, "Cheque".equals(type));
        });
        root.setPadding(new Insets(20));
        root.setPrefWidth(320);
        root.getChildren().addAll(new Label("Account type"), typeBox, investmentBox, chequeBox,
                new HBox(10, openButton, cancelButton));
    }

    private void setVisibleAndManaged(VBox box, boolean show) {
        box.setVisible(show);
        box.setManaged(show);
    }

    public Parent getRoot() { return root; }
    public String getSelectedType() { return typeBox.getValue(); }
    public String getOpeningDepositText() { return openingDepositField.getText(); }
    public String getCompanyName() { return companyNameField.getText(); }
    public String getCompanyAddress() { return companyAddressField.getText(); }
    public Button getOpenButton() { return openButton; }
    public Button getCancelButton() { return cancelButton; }
}
