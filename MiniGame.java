package ProjectPhase1;

import java.io.*;
import java.util.*;

abstract class MiniGame {
    public abstract void playGame(String userInput, String[] extraData);
}

class TriviaGame extends MiniGame {

    private static List<String[]> questionsList = new ArrayList<>();
    private static boolean isLoaded = false;
    private static final String FILE_NAME = "questions.txt";

    private String[] currentQuestion = null;  // Non-static for each instance
    private boolean waitingForAnswer = false;

    // Load questions from file (once)
    private static void loadQuestionsFromFile() {
        if (isLoaded) return;

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Output.println("Error creating questions file: " + e.getMessage());
            }
            isLoaded = true;
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length == 6) questionsList.add(parts);
                }
            }
            isLoaded = true;
        } catch (IOException e) {
            Output.println("Error loading questions: " + e.getMessage());
        }
    }

    @Override
    public void playGame(String userInput, String[] extraData) {
        loadQuestionsFromFile();

        if (questionsList.isEmpty()) {
            Output.println("No questions available to play.");
            return;
        }

        boolean isAdmin = extraData != null && extraData.length > 0 && extraData[0].equalsIgnoreCase("admin");
        if (isAdmin) {
            adminMenu(userInput);
            return;
        }

        // NORMAL USER FLOW
        if (!waitingForAnswer) {
            // Pick a random question
            currentQuestion = questionsList.get(new Random().nextInt(questionsList.size()));
            Output.println("\nTrivia Question:");
            Output.println(currentQuestion[0]);
            for (int i = 1; i <= 4; i++) {
                Output.println(i + ". " + currentQuestion[i]);
            }
            Output.println("(Enter your answer 1–4 now)");
            waitingForAnswer = true;
        } else {
            // User submitted answer
            int userAnswer;
            try {
                userAnswer = Integer.parseInt(userInput.trim());
            } catch (NumberFormatException e) {
                Output.println("Invalid input! Enter a number between 1–4.");
                return;
            }

            int correctIndex = Integer.parseInt(currentQuestion[5]);

            if (userAnswer >= 1 && userAnswer <= 4) {
                if (userAnswer == correctIndex) Output.println("Correct Answer!");
                else Output.println("Wrong! Correct was option " + correctIndex + ": " + currentQuestion[correctIndex]);
            } else {
                Output.println("Invalid option! Please enter 1–4.");
                return;
            }

            // Reset for next round
            currentQuestion = null;
            waitingForAnswer = false;
        }
    }

    // ---------------- ADMIN MENU ----------------
    private void adminMenu(String userInput) {
        Output.println("\n--- Admin Trivia Menu ---");
        Output.println("1. View All Questions");
        Output.println("2. Add New Question");
        Output.println("3. Remove Question");
        Output.println("4. Exit Admin Mode");

        int choice;
        try {
            choice = Integer.parseInt(userInput.trim());
        } catch (NumberFormatException e) {
            Output.println("Invalid input! Enter 1–4.");
            return;
        }

        switch (choice) {
            case 1 -> viewAllQuestions();
            case 2 -> addQuestion();
            case 3 -> removeQuestion();
            case 4 -> Output.println("Exiting Admin Mode...");
            default -> Output.println("Invalid choice!");
        }
    }

    private void viewAllQuestions() {
        Output.println("\n=== All Questions ===");
        int i = 1;
        for (String[] q : questionsList) {
            Output.println(i + ". " + q[0]);
            for (int j = 1; j <= 4; j++) Output.println("   " + j + ". " + q[j]);
            Output.println("   Correct Answer: Option " + q[5]);
            i++;
        }
    }

    private void addQuestion() {
        try (Scanner sc = new Scanner(System.in)) {
            Output.print("Enter question: ");
            String question = sc.nextLine().trim();
            String[] options = new String[4];
            for (int i = 0; i < 4; i++) {
                Output.print("Option " + (i + 1) + ": ");
                options[i] = sc.nextLine().trim();
            }
            Output.print("Enter correct option number (1-4): ");
            int correct = Integer.parseInt(sc.nextLine().trim());
            if (correct < 1 || correct > 4) {
                Output.println("Invalid correct option number!");
                return;
            }

            String newLine = question + "," + options[0] + "," + options[1] + "," + options[2] + "," + options[3] + "," + correct;

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                bw.write(newLine);
                bw.newLine();
            }

            questionsList.add(new String[]{question, options[0], options[1], options[2], options[3], String.valueOf(correct)});
            Output.println("Question added successfully!");
        } catch (Exception e) {
            Output.println("Error adding question: " + e.getMessage());
        }
    }

    private void removeQuestion() {
        try (Scanner sc = new Scanner(System.in)) {
            Output.print("Enter question number to remove: ");
            int index = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (index < 0 || index >= questionsList.size()) {
                Output.println("Invalid question number!");
                return;
            }

            questionsList.remove(index);

            // Rewrite file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (String[] q : questionsList) {
                    bw.write(String.join(",", q));
                    bw.newLine();
                }
            }

            Output.println("Question removed successfully!");
        } catch (Exception e) {
            Output.println("Error removing question: " + e.getMessage());
        }
    }
}
