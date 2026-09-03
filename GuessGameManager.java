package com.example.backup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuessGameManager {

    private final int targetNumber;
    private int attempts;
    private boolean isGameOver;
    private final List<String> history;
    private static final int MIN_RANGE = 1;
    private static final int MAX_RANGE = 100;
    private static final int MAX_ATTEMPTS = 5;


    public GuessGameManager() {
        Random random = new Random();
        this.targetNumber = random.nextInt(MAX_RANGE) + MIN_RANGE; // Generates number between 1 and 100
        this.attempts = 0;
        this.isGameOver = false;
        this.history = new ArrayList<>();
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getAttempts() {
        return attempts;
    }

    public List<String> getHistory() {
        return history;
    }

    /**
     * Processes a user's guess and returns the appropriate feedback message.
     * @param guess The user's number guess.
     * @return A feedback string (e.g., "Too high!", "Too low!", "Correct!").
     */
    public String checkGuess(int guess) {

        if (isGameOver) {
            return "Game over. Please start a new game.";
        }

        attempts++;

        // Check attempt limit FIRST
        if (attempts > MAX_ATTEMPTS) {
            isGameOver = true;
            return "No attempts left.Target number was "+targetNumber+" Game over!";
        }

        String feedback;

        if (guess < MIN_RANGE || guess > MAX_RANGE) {
            feedback = "Invalid guess: Must be between "
                    + MIN_RANGE + " and " + MAX_RANGE + ".";
        }
        else if (guess < targetNumber) {
            feedback = "Too low! Try a higher number.";
        }
        else if (guess > targetNumber) {
            feedback = "Too high! Try a lower number.";
        }
        else {
            isGameOver = true;
            feedback = "Correct! You guessed the number "
                    + targetNumber + " in " + attempts + " attempts.";
        }

        history.add("Attempt " + attempts + ": " + guess + " → " + feedback);

        // Final attempt used but still incorrect
        if (!isGameOver && attempts == MAX_ATTEMPTS) {
            isGameOver = true;
            history.add("Game Over! Maximum attempts reached.");
            return feedback + " | Game Over! You've used all 5 attempts.";
        }

        return feedback;
    }

    /** Resets the game state by creating a new instance with a new target number. */
    public GuessGameManager startNewGame() {
        return new GuessGameManager();
    }
}
