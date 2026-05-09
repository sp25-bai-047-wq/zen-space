package com.example.backup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ZenSpace extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Zen Space - Login");

        loadLoginView();
        primaryStage.show();
    }

    public void loadLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
            BorderPane root = loader.load();

            LoginController controller = loader.getController();
            controller.setApp(this);

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Zen Space - Login");

        } catch (Exception e) {
            System.err.println("Error loading Login View:");
            e.printStackTrace();
        }
    }

    public void loadMainAppView(User loggedInUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainZenSpaceView.fxml"));
            BorderPane root = loader.load();

            MainController controller = loader.getController();
            controller.initData(loggedInUser, this);

            primaryStage.setTitle("Zen Space - Dashboard (" + loggedInUser.getUsername() + ")");
            primaryStage.setScene(new Scene(root));

        } catch (Exception e) {
            System.err.println("Error loading Main App View:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}