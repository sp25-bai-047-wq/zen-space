package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.util.List;

public class AddViewQuoteController {

    @FXML private ListView<Quote> quotesListView;
    @FXML private TextField quoteTextInput;
    @FXML private TextField authorInput;
    @FXML private Label addFeedbackLabel;
    @FXML private Label listFeedbackLabel;


    private User loggedInUser;
    private MainController mainController;
    private QuoteGenerator quoteGeneratorModel;

    public void initData(User user, MainController controller) {
        this.loggedInUser = user;
        this.mainController = controller;
        this.quoteGeneratorModel = new QuoteGenerator(user);

        setupListView();
        refreshQuoteList();
    }

    private void setupListView() {
        quotesListView.setCellFactory(lv -> new javafx.scene.control.ListCell<Quote>() {
            @Override
            protected void updateItem(Quote item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getQuoteText() + " — (" + item.getAuthor() + ")");

                setWrapText(true);
            }
        });
    }

    private void refreshQuoteList() {
        List<Quote> quotes = quoteGeneratorModel.getAllQuotes();

        ObservableList<Quote> quoteItems = FXCollections.observableArrayList(quotes);
        quotesListView.setItems(quoteItems);

        if (quotes.isEmpty()) {
            listFeedbackLabel.setText("No quotes found. Add a new one on the right!");
        } else {
            listFeedbackLabel.setText("Showing " + quotes.size() + " quotes.");
        }
    }


    @FXML
    private void handleAddQuote(ActionEvent event) {
        String quoteText = quoteTextInput.getText().trim();
        String author = authorInput.getText().trim();

        if (quoteText.isEmpty()) {
            addFeedbackLabel.setText("Quote text is required!");
            return;
        }
        String statusMessage = quoteGeneratorModel.addQuote(quoteText, author);

        addFeedbackLabel.setText(statusMessage);

        if (statusMessage.startsWith("Quote successfully")) {
            quoteTextInput.clear();
            authorInput.clear();
            refreshQuoteList();
        }
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        if (mainController != null) {
            mainController.handleQuotesClick();
        }
    }
}