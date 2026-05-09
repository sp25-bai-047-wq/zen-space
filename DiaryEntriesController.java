package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;

import java.util.List;

public class DiaryEntriesController {

    @FXML private ListView<String> entryListView;
    @FXML private Label entryTitleLabel;
    @FXML private Label entryDateLabel;
    @FXML private TextArea entryContentArea;
    @FXML private Button deleteButton;

    private Diary diaryModel;
    private List<DiaryEntry> entries;
    private MainController mainController;
    private DiaryEntry currentlySelectedEntry;

    @FXML
    public void initialize() {
        setupListViewListener();
        deleteButton.setVisible(false);
    }

    public void initData(User user, MainController controller) {
        this.diaryModel = new Diary(user.getUsername());
        this.mainController = controller;

        loadEntries();
    }

    private void loadEntries() {
        entries = diaryModel.getEntries();
        entryListView.getItems().clear();

        for (DiaryEntry entry : entries) {
            entryListView.getItems().add(
                    entry.getFormattedDate() + " - " + entry.getTitle()
            );
        }

        if (entries.isEmpty()) {
            clearEntryDetails();
        }
    }

    private void setupListViewListener() {
        entryListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int index = entryListView.getSelectionModel().getSelectedIndex();

                currentlySelectedEntry = entries.get(index);

                displayEntryDetails(currentlySelectedEntry);
                deleteButton.setVisible(true);
            } else {
                clearEntryDetails();
                deleteButton.setVisible(false);
            }
        });
    }

    private void displayEntryDetails(DiaryEntry entry) {
        entryTitleLabel.setText(entry.getTitle());
        entryDateLabel.setText(entry.getFormattedDate());

        String savedFont = entry.getFontName();

        String fontStyle = String.format("-fx-font-family: '%s'; -fx-font-size: 16px;", savedFont);

        entryContentArea.setStyle(fontStyle);

        entryContentArea.setText(entry.getContent());
    }

    private void clearEntryDetails() {
        entryTitleLabel.setText("Title: (Select Entry)");
        entryDateLabel.setText("Date: --");
        entryContentArea.setText("Entry content will appear here...");
        currentlySelectedEntry = null;
    }

    @FXML
    private void handleDeleteEntry(ActionEvent event) {
        if (currentlySelectedEntry != null) {
            boolean success = diaryModel.deleteEntry(currentlySelectedEntry);

            if (success) {
                loadEntries();
                clearEntryDetails();
                deleteButton.setVisible(false);
            } else {
                System.err.println("Failed to delete diary entry: " + currentlySelectedEntry.getTitle());
            }
        }
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        if (mainController != null) {
            mainController.handleDiaryGameClick();
        }
    }
}