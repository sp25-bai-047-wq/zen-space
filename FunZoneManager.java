package ProjectPhase1;

import java.io.*;
import java.util.*;

public class FunZoneManager {

    private String username;
    private final String FILE_PREFIX_GUESS = "guess_";
    private final String FILE_PREFIX_TRIVIA = "trivia_";

    public FunZoneManager(String username) {
        this.username = username;
    }

    // ============== DISPLAY MENU ==============
    public void displayMenu() {
        Output.println("\n=== FUN ZONE ===");
        Output.println("1. Guess the Number");
        Output.println("2. Trivia Challenge");
        Output.println("3. Back");
        Output.print("Enter choice: ");
    }

    // ============== RUN GAME BY CHOICE ==============
    public void runGameByChoice(int choice, int[] userInputs, String[] adminFlag) {
        switch (choice) {
            case 1 -> { // Guess the Number
                int userGuess = userInputs[0];
                int secretNumber = (int) (Math.random() * 10) + 1;
                recordGuessGame(userGuess, secretNumber);
                Output.println("Secret Number: " + secretNumber);
                Output.println(userGuess == secretNumber ? "You Win!" : "You Lose!");
            }
            case 2 -> { // Trivia Challenge
                int userAnswer = userInputs[0];
                // Example trivia question
                String question = "2 + 2 = ?";
                int correctAnswer = 4;
                recordTriviaGame(question, userAnswer, correctAnswer);
                Output.println(userAnswer == correctAnswer ? "Correct!" : "Wrong!");
            }
            default -> Output.println("Invalid game choice.");
        }
    }

    // ================= GUESS GAME =================
    public void recordGuessGame(int userGuess, int secretNumber) {
        String fileName = FILE_PREFIX_GUESS + username + ".txt";
        String result = (userGuess == secretNumber) ? "Win" : "Lose";
        String line = "Guess: " + userGuess + ", Secret: " + secretNumber + ", Result: " + result;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(line + "\n");
        } catch (IOException e) {
            Output.println("Error saving Guess Game data: " + e.getMessage());
        }
    }

    public void viewGuessStats(boolean isAdmin) {
        if (isAdmin) {
            File dir = new File(".");
            File[] files = dir.listFiles((d, name) -> name.startsWith(FILE_PREFIX_GUESS));
            if (files == null || files.length == 0) {
                Output.println("No Guess Game records found.");
                return;
            }
            for (File f : files) {
                Output.println("\n--- " + f.getName() + " ---");
                try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                    String line;
                    while ((line = br.readLine()) != null) System.out.println(line);
                } catch (IOException e) {
                    Output.println("Error reading file " + f.getName() + ": " + e.getMessage());
                }
            }
        } else {
            String fileName = FILE_PREFIX_GUESS + username + ".txt";
            File file = new File(fileName);
            if (!file.exists()) {
                Output.println("No Guess Game records found.");
                return;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                Output.println("\n=== Your Guess Game Records ===");
                while ((line = br.readLine()) != null) System.out.println(line);
            } catch (IOException e) {
                Output.println("Error reading Guess Game file: " + e.getMessage());
            }
        }
    }

    // ================= TRIVIA GAME =================
    public void recordTriviaGame(String question, int userAnswer, int correctAnswer) {
        String fileName = FILE_PREFIX_TRIVIA + username + ".txt";
        String result = (userAnswer == correctAnswer) ? "Correct" : "Wrong";
        String line = "Q: " + question + " | Your: " + userAnswer + " | Correct: " + correctAnswer + " | Result: " + result;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(line + "\n");
        } catch (IOException e) {
            Output.println("Error saving Trivia Game data: " + e.getMessage());
        }
    }

    public void viewTriviaStats(boolean isAdmin) {
        if (isAdmin) {
            File dir = new File(".");
            File[] files = dir.listFiles((d, name) -> name.startsWith(FILE_PREFIX_TRIVIA));
            if (files == null || files.length == 0) {
                Output.println("No Trivia Game records found.");
                return;
            }
            for (File f : files) {
                Output.println("\n--- " + f.getName() + " ---");
                try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                    String line;
                    while ((line = br.readLine()) != null) System.out.println(line);
                } catch (IOException e) {
                    Output.println("Error reading file " + f.getName() + ": " + e.getMessage());
                }
            }
        } else {
            String fileName = FILE_PREFIX_TRIVIA + username + ".txt";
            File file = new File(fileName);
            if (!file.exists()) {
                Output.println("No Trivia Game records found.");
                return;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                Output.println("\n=== Your Trivia Game Records ===");
                while ((line = br.readLine()) != null) System.out.println(line);
            } catch (IOException e) {
                Output.println("Error reading Trivia Game file: " + e.getMessage());
            }
        }
    }
}
