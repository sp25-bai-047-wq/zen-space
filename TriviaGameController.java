package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TriviaGameController {

    @FXML private Label questionNumberLabel;
    @FXML private Label questionLabel;
    @FXML private VBox optionsVBox;
    @FXML private Label feedbackLabel;
    @FXML private Button submitButton;
    @FXML private Button nextQuestionButton;

    private MainController mainController;
    private TriviaGameManager gameManager;
    private List<CheckBox> currentCheckBoxes;

    public void initData(User user, MainController controller) {
        this.mainController = controller;
        startNewGameSession();
    }

    private void startNewGameSession() {
        this.gameManager = new TriviaGameManager();
        this.currentCheckBoxes = new ArrayList<>();

        // Reset UI
        submitButton.setVisible(true);
        submitButton.setDisable(false);
        nextQuestionButton.setVisible(false);
        feedbackLabel.setText("Select your answer(s) and submit.");

        loadCurrentQuestion();
    }

    private void loadCurrentQuestion() {
        TriviaQuestion currentQuestion = gameManager.getCurrentQuestion();

        if (currentQuestion != null) {
            questionNumberLabel.setText("Question " + gameManager.getCurrentQuestionNumber());
            questionLabel.setText(currentQuestion.getQuestionText());
            optionsVBox.getChildren().clear();
            currentCheckBoxes.clear();

            int index = 0;
            for (String option : currentQuestion.getOptions()) {
                CheckBox cb = new CheckBox(option);
                cb.setUserData(index);
                optionsVBox.getChildren().add(cb);
                currentCheckBoxes.add(cb);
                index++;
            }
        } else {
            displayGameOver();
        }
    }

    private void displayGameOver() {
        questionNumberLabel.setText("GAME OVER");
        questionLabel.setText("Quiz finished! Your final score is: " + gameManager.getScore() + " / " + gameManager.getTotalQuestions());
        optionsVBox.getChildren().clear();
        feedbackLabel.setText("Press 'Next Question' (Start New Game) to play again.");

        submitButton.setVisible(false);
        nextQuestionButton.setVisible(true);
        nextQuestionButton.setText("Start New Game");
    }



    @FXML
    private void handleSubmitAnswer(ActionEvent event) {
        if (gameManager.isGameOver()) return;

        List<Integer> userSelectedIndices = currentCheckBoxes.stream()
                .filter(CheckBox::isSelected)
                .map(cb -> (Integer) cb.getUserData())
                .collect(Collectors.toList());

        if (userSelectedIndices.isEmpty()) {
            feedbackLabel.setText("Please select at least one answer.");
            return;
        }

        boolean isCorrect = gameManager.checkAnswer(userSelectedIndices);

        if (isCorrect) {
            feedbackLabel.setText("✅ Correct Answer! Score: " + gameManager.getScore());
        } else {
            feedbackLabel.setText("❌ Incorrect Answer. Score: " + gameManager.getScore());
        }

        currentCheckBoxes.forEach(cb -> cb.setDisable(true));
        submitButton.setDisable(true);
        nextQuestionButton.setVisible(true);
    }

    @FXML
    private void handleNextQuestion(ActionEvent event) {
        if (gameManager.isGameOver()) {
            startNewGameSession();
        } else {
            gameManager.goToNextQuestion();
            submitButton.setDisable(false);
            nextQuestionButton.setVisible(false);
            loadCurrentQuestion();
            feedbackLabel.setText("Select your answer(s) and submit.");
        }
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        if (mainController != null) {
            mainController.handleFunZoneClick();
        }
    }
}