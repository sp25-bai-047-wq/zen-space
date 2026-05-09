package com.example.backup;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class QuoteMenuController {

    private User loggedInUser;
    private MainController mainController; // Reference to the parent controller for view switching

    public void initData(User user, MainController controller) {
        this.loggedInUser = user;
        this.mainController = controller;
    }

    @FXML
    private void handleRandomQuoteClick(ActionEvent event) {
        // Option 1: Show Random Quote (Requires initialization with User)
        // We will call a specialized loader in MainController for this.
        if (mainController != null) {
            mainController.loadRandomQuoteView(); // Method to be added to MainController
        }
    }

    @FXML
    private void handleAddViewQuotesClick(ActionEvent event) {
        // Option 2: Add/View All Quotes (Requires initialization with User)
        // We will call a specialized loader in MainController for this.
        if (mainController != null) {
            mainController.loadAddViewQuoteView(); // Method to be added to MainController
        }
    }

    /** * Utility method to allow other controllers to return to this menu.
     * Called by RandomQuoteController and AddQuoteController.
     */
    public void returnToMenu() {
        if (mainController != null) {
            mainController.handleQuotesClick();
        }
    }
}