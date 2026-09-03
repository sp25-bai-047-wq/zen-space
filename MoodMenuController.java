package com.example.backup;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class MoodMenuController {
    private User loggedInUser;
    private MainController mainController;

    public void initData(User user, MainController controller) {
        this.loggedInUser = user;
        this.mainController = controller;
    }

    @FXML
    private void handleTrackToday(ActionEvent event) {
        mainController.loadMoodTrackerView();
    }

    @FXML
    private void handleViewPastMoods(ActionEvent event) {
        mainController.loadMoodHistoryView();
    }
}
