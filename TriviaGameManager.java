package com.example.backup;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import static java.util.Collections.shuffle;

public class TriviaGameManager {

    private static final String TRIVIA_FILE_NAME = "questions.txt";
    private final List<TriviaQuestion> questionBank;
    private int currentQuestionIndex;
    private int score;
    private boolean isGameOver;


    public TriviaGameManager() {
        this.questionBank = new ArrayList<>();
        loadQuestionsFromFile(); // Load questions from the file

        if (questionBank.isEmpty()) {
            addFailsafeQuestions(); // Load basic questions if the file is missing/empty/corrupted
        }

        shuffle(this.questionBank);
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.isGameOver = false;
    }

    private void addFailsafeQuestions() {
        // Ensure the game doesn't crash if file loading fails
        questionBank.add(new TriviaQuestion(
                "Which of the following are NOT programming languages?",
                List.of("Python", "CSS", "C++", "HTML"),
                List.of(1, 3) // CSS, HTML
        ));
        questionBank.add(new TriviaQuestion(
                "What country is Tokyo in?",
                List.of("China", "South Korea", "Japan", "Thailand"),
                List.of(2)
        ));
    }

    private void loadQuestionsFromFile() {
        File file = new File(TRIVIA_FILE_NAME);
        System.out.println("Looking for trivia file at: " + file.getAbsolutePath());
        if (!file.exists()) {
            System.err.println("Trivia file not found: " + TRIVIA_FILE_NAME);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue; // skip empty lines

                try {
                    // Split by | flexibly (ignore spaces)
                    String[] parts = line.split("\\|");
                    if (parts.length != 3) {
                        System.err.println("Line " + lineNumber + " ignored: does not have 3 parts -> " + line);
                        continue;
                    }

                    // Extract question text
                    String questionText = parts[0].replaceFirst("(?i)Q:\\s*", "").trim();
                    if (questionText.isEmpty()) {
                        System.err.println("Line " + lineNumber + " ignored: empty question text -> " + line);
                        continue;
                    }

                    // Extract options
                    String optionsPart = parts[1].replaceFirst("(?i)Options:\\s*", "").trim();
                    List<String> options = Arrays.asList(optionsPart.split("\\s*,\\s*"));
                    if (options.isEmpty()) {
                        System.err.println("Line " + lineNumber + " ignored: no options found -> " + line);
                        continue;
                    }

                    // Extract correct indices
                    String correctPart = parts[2].replaceFirst("(?i)Correct:\\s*", "").trim();
                    List<Integer> correctIndices = new ArrayList<>();
                    for (String s : correctPart.split("\\s*,\\s*")) {
                        try {
                            correctIndices.add(Integer.parseInt(s));
                        } catch (NumberFormatException nfe) {
                            System.err.println("Line " + lineNumber + " warning: invalid correct index '" + s + "' ignored");
                        }
                    }

                    if (correctIndices.isEmpty()) {
                        System.err.println("Line " + lineNumber + " ignored: no valid correct indices -> " + line);
                        continue;
                    }

                    // Add question
                    TriviaQuestion tq = new TriviaQuestion(questionText, options, correctIndices);
                    questionBank.add(tq);

                } catch (Exception e) {
                    System.err.println("Line " + lineNumber + " skipped due to error: " + e.getMessage());
                }
            }

            System.out.println("Loaded " + questionBank.size() + " trivia questions successfully.");

        } catch (IOException e) {
            System.err.println("Error reading trivia file: " + e.getMessage());
        }
    }


    // ... (rest of the TriviaGameManager class remains the same: getCurrentQuestion, checkAnswer, goToNextQuestion, etc.)

    public TriviaQuestion getCurrentQuestion() {
        if (currentQuestionIndex < questionBank.size()) {
            return questionBank.get(currentQuestionIndex);
        }
        return null;
    }

    public int getTotalQuestions() {
        return questionBank.size();
    }

    public int getCurrentQuestionNumber() {
        return currentQuestionIndex + 1;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getScore() {
        return score;
    }

    public boolean checkAnswer(List<Integer> userSelectedIndices) {
        if (isGameOver) return false;

        TriviaQuestion currentQuestion = getCurrentQuestion();
        if (currentQuestion == null) return false;

        List<Integer> correctIndices = new ArrayList<>(currentQuestion.getCorrectIndices());
        Collections.sort(userSelectedIndices);
        Collections.sort(correctIndices);

        boolean correct = userSelectedIndices.equals(correctIndices);

        if (correct) {
            score++;
        }
        return correct;
    }

    public void goToNextQuestion() {
        if (isGameOver) return;

        currentQuestionIndex++;
        if (currentQuestionIndex >= questionBank.size()) {
            isGameOver = true;
        }
    }

    public TriviaGameManager startNewGame() {
        return new TriviaGameManager();
    }
}
