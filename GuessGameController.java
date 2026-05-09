package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.util.stream.Collectors;

public class GuessGameController {

    // === FXML Elements ===
    @FXML private Label instructionLabel;
    @FXML private TextField guessInput;
    @FXML private Label feedbackLabel;
    @FXML private TextArea historyArea;
    @FXML private Button nextGuessButton;
    @FXML private Button submitButton;

    // === Models and State ===
    private MainController mainController;
    private GuessGameManager gameManager;

    // --- Initialization ---
    public void initData(User user, MainController controller) {
        this.mainController = controller;
        // The GuessGameManager does not require the User object for persistence
        // (unlike Diary or ToDoList), so we just instantiate the game.
        startNewGameSession();
    }

    private void startNewGameSession() {
        this.gameManager = new GuessGameManager();

        // Reset UI elements
        guessInput.clear();
        historyArea.clear();
        feedbackLabel.setText("Guess a number between 1 and 100.");
        nextGuessButton.setVisible(false);
        submitButton.setDisable(false);
        guessInput.setDisable(false);
        instructionLabel.setText("I'm thinking of a number between 1 and 100. Try to guess it!");
    }

    private void updateHistory() {
        // Collect history list into a single string for the TextArea
        String historyText = gameManager.getHistory().stream()
                .collect(Collectors.joining("\n"));
        historyArea.setText(historyText);
        // Scroll to bottom
        historyArea.setScrollTop(Double.MAX_VALUE);
    }

    // --- Action Handlers ---

    @FXML
    private void handleSubmitGuess(ActionEvent event) {
        if (gameManager.isGameOver()) {
            feedbackLabel.setText("Game over! Click 'Start New Game' to play again.");
            return;
        }

        int guess;
        try {
            guess = Integer.parseInt(guessInput.getText().trim());
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Invalid input. Please enter a whole number.");
            return;
        }

        // Clear input after processing
        guessInput.clear();

        // Process guess through the Model
        String feedback = gameManager.checkGuess(guess);
        feedbackLabel.setText(feedback);
        updateHistory();

        // Check if game ended
        if (gameManager.isGameOver()) {
            submitButton.setDisable(true);
            guessInput.setDisable(true);
            nextGuessButton.setVisible(true); // Show the button to start a new game
            instructionLabel.setText("Game Finished! Took " + gameManager.getAttempts() + " attempts.");
        }
    }

    @FXML
    private void handleNextGuess(ActionEvent event) {
        // This button handles starting a new session
        startNewGameSession();
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        // Call the FunZoneController's navigation method to return to the menu
        if (mainController != null) {
            mainController.handleFunZoneClick();
        }
    }
}