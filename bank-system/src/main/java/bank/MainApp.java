package bank;

import bank.controller.LoginController;
import bank.dao.DatabaseConnection;
import bank.dao.SampleData;
import javafx.application.Application;
import javafx.stage.Stage;

/** Starts the application: create tables, add sample data, show the login screen. */
public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        DatabaseConnection.initSchema();
        SampleData.insertIfEmpty();
        new LoginController(stage).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
