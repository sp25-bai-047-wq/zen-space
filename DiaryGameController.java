package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class DiaryGameController {

    @FXML private VBox rootGameContainer;
    @FXML private Label levelLabel;
    @FXML private HBox lettersContainer;
    @FXML private FlowPane wordsContainer;
    @FXML private TextField wordInput;
    @FXML private Text feedbackText;
    @FXML private Label gameStatusLabel;

    private GameManager.Level currentLevel;
    private int currentLevelNum;
    private Button[] wordButtons;
    private User loggedInUser;
    private MainController mainController;
    private Diary diaryModel;

    private boolean entryUnlockedThisSession = false;

    @FXML
    public void initialize() {
    }

    public void initData(User user, MainController controller) {
        this.loggedInUser = user;
        this.mainController = controller;
        this.diaryModel = new Diary(user.getUsername());

        if (controller.isEntryUnlockedForUser(user)) {
            entryUnlockedThisSession = true;
        }

        loadNewLevel();
    }


    private void loadNewLevel() {
        if (entryUnlockedThisSession) {
            setupUnlockedUI();
            loadDiaryFormView();
            return;
        }

        currentLevelNum = GameManager.ProgressManager.loadProgress(loggedInUser.getUsername());
        currentLevel = GameManager.startLevel(currentLevelNum);

        if (currentLevel != null) {
            setupUIForLevel(currentLevelNum);
            updateProgressDisplay();
            gameStatusLabel.setText("First complete the level to add entry (Level " + currentLevelNum + ").");

            if (currentLevel.isComplete()) {
                handleLevelCompletion();
            }
        } else {
            gameStatusLabel.setText("All word game levels complete! You can still write entries.");
            loadDiaryFormView();
        }
    }

    private void setupUnlockedUI() {
        levelLabel.setText("Entry Unlocked");
        feedbackText.setText("");
        wordInput.clear();
        wordInput.setDisable(true);

        lettersContainer.getChildren().clear();
        wordsContainer.getChildren().clear();
        gameStatusLabel.setText("*** ENTRY UNLOCKED! You can write your diary entry. ***");
    }

    private void setupUIForLevel(int levelNum) {
        levelLabel.setText("Level " + levelNum);
        feedbackText.setText("");
        wordInput.clear();
        wordInput.setDisable(false);

        setupLettersDisplay(currentLevel.getAvailableLetters());
        setupWordsDisplay(currentLevel.getTotalCount());
    }

    private void setupLettersDisplay(String letters) {
        lettersContainer.getChildren().clear();
        for (char c : letters.toCharArray()) {
            Button letterButton = new Button(String.valueOf(c));
            letterButton.setStyle("-fx-font-size: 16px; -fx-background-color: #ADD8E6; -fx-text-fill: black;");
            lettersContainer.getChildren().add(letterButton);
        }
    }

    private void setupWordsDisplay(int totalWords) {
        wordsContainer.getChildren().clear();
        wordButtons = new Button[totalWords];

        String[] targetWords = currentLevel.getTargetWords();

        for (int i = 0; i < totalWords; i++) {
            String word = targetWords[i].toUpperCase();

            Button wordButton = new Button();
            wordButton.setText("_".repeat(word.length()));

            wordButton.setStyle("-fx-font-size: 14px; -fx-background-color: #E0E0E0; -fx-text-fill: grey;");
            wordsContainer.getChildren().add(wordButton);
            wordButtons[i] = wordButton;
        }
    }

    private void updateProgressDisplay() {
        String[] targetWords = currentLevel.getTargetWords();
        boolean[] foundStatus = currentLevel.getFoundStatus();

        if (targetWords == null || foundStatus == null) return;

        for (int i = 0; i < targetWords.length; i++) {
            String word = targetWords[i].toUpperCase();
            Button button = wordButtons[i];

            if (foundStatus[i]) {
                button.setText(word);
                button.setStyle("-fx-font-size: 14px; -fx-background-color: #90EE90; -fx-text-fill: black; -fx-font-weight: bold;");
            }
        }

        if (currentLevel.isComplete()) {
            handleLevelCompletion();
        }
    }

    // --- INPUT HANDLING ---
    @FXML
    private void handleSubmitWord(ActionEvent event) {
        String input = wordInput.getText().trim();
        wordInput.clear();

        if (currentLevel == null || currentLevel.isComplete()) {
            gameStatusLabel.setText("Level already complete or not loaded.");
            return;
        }

        if (input.isEmpty()) {
            feedbackText.setText("Please enter a word.");
            return;
        }

        int result = GameManager.processInput(currentLevel, input);

        switch (result) {
            case 0:
                feedbackText.setText("✅ Correct word: " + input.toUpperCase() + "!");
                updateProgressDisplay();
                break;
            case 1:
                feedbackText.setText(input.toUpperCase() + " already found.");
                break;
            case 2:
                feedbackText.setText(input.toUpperCase() + " is not a target word.");
                break;
            case 3:
                feedbackText.setText("Invalid letters. You must use only the available letters.");
                break;
            default:
                feedbackText.setText("An internal error occurred (Code: " + result + ")");
        }
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        if (mainController != null) {
            mainController.handleDiaryGameClick();
        }
    }

    // --- Level Completion Handler ---
    private void handleLevelCompletion() {
        entryUnlockedThisSession = true;
        mainController.setEntryUnlockedForUser(loggedInUser, true);

        gameStatusLabel.setText("*** LEVEL COMPLETE! Diary entry unlocked. ***");
        feedbackText.setText("Level Complete! Diary entry unlocked.");
        wordInput.setDisable(true);

        loadDiaryFormView();
    }

    // --- Dynamic Loading of Diary Form ---
    private void loadDiaryFormView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DiaryFormView.fxml"));
            Parent diaryFormView = loader.load();

            DiaryFormController controller = loader.getController();
            controller.initData(loggedInUser, diaryModel, mainController, this);

            rootGameContainer.getChildren().setAll(diaryFormView);

            VBox.setVgrow(diaryFormView, javafx.scene.layout.Priority.ALWAYS);

        } catch (IOException e) {
            System.err.println("Error loading Diary Form View:");
            e.printStackTrace();
            rootGameContainer.getChildren().setAll(new Label("ERROR: Could not load Diary Form."));
        }
    }

    public void entrySavedSuccessfully() {
        entryUnlockedThisSession = false;
        mainController.setEntryUnlockedForUser(loggedInUser, false);

        GameManager.ProgressManager.saveProgress(loggedInUser.getUsername(), currentLevelNum + 1);
    }
}
