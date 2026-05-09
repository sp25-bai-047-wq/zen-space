package com.example.backup;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class FunZoneController {

    private User loggedInUser;
    private MainController mainController;

    public void initData(User user, MainController controller) {
        this.loggedInUser = user;
        this.mainController = controller;
    }

    @FXML
    private void handleGuessGameClick(ActionEvent event) {

        if (mainController != null) {
            mainController.loadGuessGameView();
        }
    }

    @FXML
    private void handleTriviaGameClick(ActionEvent event) {
        if (mainController != null) {
            mainController.loadTriviaGameView();
        }
    }

    public void returnToMenu() {
        if (mainController != null) {
            mainController.handleFunZoneClick();
        }
    }
}
