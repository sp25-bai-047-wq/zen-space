package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class RandomQuoteController {

    @FXML private Label quoteTextLabel;
    @FXML private Label quoteAuthorLabel;

    private User loggedInUser;
    private MainController mainController;
    private QuoteGenerator quoteGeneratorModel;

    public void initData(User user, MainController controller) {
        this.loggedInUser = user;
        this.mainController = controller;
        this.quoteGeneratorModel = new QuoteGenerator(user);

        displayRandomQuote();
    }

    private void displayRandomQuote() {
        Quote quote = quoteGeneratorModel.getRandomQuote();

        if (quote != null) {
            quoteTextLabel.setText("\"" + quote.getQuoteText() + "\"");
            quoteAuthorLabel.setText("- " + quote.getAuthor());
        } else {
            quoteTextLabel.setText("No quotes found. Please add a quote first!");
            quoteAuthorLabel.setText("");
        }
    }
    @FXML
    private void handleNewQuoteClick(ActionEvent event) {
        displayRandomQuote();
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        if (mainController != null) {
            mainController.handleQuotesClick();
        }
    }
}