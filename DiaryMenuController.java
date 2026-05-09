package com.example.backup;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class DiaryMenuController {

    private User loggedInUser;
    private MainController mainController; // Reference to the parent controller for view switching

    public void initData(User user, MainController controller) {
        this.loggedInUser = user;
        this.mainController = controller;
    }


    @FXML
    private void handleViewEntries(ActionEvent event) {
        // Option 1: View Entries
        mainController.loadDiaryEntriesView();
    }

    @FXML
    private void handleAddEntryGame(ActionEvent event) {
        // Option 2: Add Entry (Start Game)
        // CRITICAL FIX: Call the specialized method that handles initialization
        mainController.loadDiaryGameView();
    }
}
