package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.collections.FXCollections;

public class DiaryFormController {

    @FXML private TextField dateInput;
    @FXML private TextField titleInput;
    @FXML private TextArea contentInput;
    @FXML private Label feedbackLabel;
    @FXML private ComboBox<String> fontComboBox;


    private User loggedInUser;
    private Diary diaryModel;
    private MainController mainController;

    private DiaryGameController gameController;

    @FXML
    public void initialize() {
        fontComboBox.setItems(FXCollections.observableArrayList(Font.getFontNames()));
        fontComboBox.setValue("System");

        contentInput.setStyle("-fx-font-size: 14px;");
        titleInput.setStyle("-fx-font-size: 14px;");
    }

    public void initData(User user, Diary diary, MainController controller, DiaryGameController gameController) {
        this.loggedInUser = user;
        this.diaryModel = diary;
        this.mainController = controller;
        this.gameController = gameController;
        feedbackLabel.setText("Complete your entry, then save it.");
        handleFontSelection(null);
    }

    @FXML
    private void handleFontSelection(ActionEvent event) {
        String selectedFontName = fontComboBox.getSelectionModel().getSelectedItem();

        if (selectedFontName != null) {
            String fontStyle = "-fx-font-family: '" + selectedFontName + "'; -fx-font-size: 14px;";

            contentInput.setStyle(fontStyle);
            titleInput.setStyle(fontStyle);
        }
    }

    @FXML
    private void handleSaveEntry(ActionEvent event) {
        String date = dateInput.getText().trim();
        String title = titleInput.getText().trim();
        String content = contentInput.getText().trim();

        if (date.isEmpty() || title.isEmpty() || content.isEmpty()) {
            feedbackLabel.setText("Error: All fields must be filled out!");
            return;
        }

        String selectedFontName = fontComboBox.getSelectionModel().getSelectedItem();
        if (selectedFontName == null) {
            selectedFontName = "System";
        }

        DiaryEntry newEntry = new DiaryEntry(title, content, date, selectedFontName);


        boolean success = diaryModel.addEntry(newEntry);

        if (success) {
            feedbackLabel.setText("✅ Entry saved successfully! Returning to menu...");

            if (gameController != null) {
                gameController.entrySavedSuccessfully();
            }

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> {
                if (mainController != null) {
                    mainController.handleDiaryGameClick();
                }
            });
            pause.play();

            dateInput.clear();
            titleInput.clear();
            contentInput.clear();

        } else {
            feedbackLabel.setText("❌ Error saving entry. Check console for file errors.");
        }
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        if (mainController != null) {
            mainController.handleDiaryGameClick();
        }
    }
}