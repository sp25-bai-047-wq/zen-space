package ProjectPhase1;

import java.io.*;
import java.util.*;

public class AdminDashboard {

    private final String ADMIN_USERNAME;
    private final String ADMIN_PASSWORD;

    public AdminDashboard(String username, String password) {
        this.ADMIN_USERNAME = username;
        this.ADMIN_PASSWORD = password;
    }

    // ================== LOGIN ==================
    public boolean login(String username, String password) {
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            Output.println("Admin login successful! Full access granted.");
            return true;
        } else {
            Output.println("Invalid admin credentials!");
            return false;
        }
    }

    // ================== MOODS ==================
    public void viewAllMoods() {
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith("moods_") && name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            Output.println("No moods recorded.");
            return;
        }

        for (File f : files) {
            Output.println("\n--- " + f.getName() + " ---");
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) Output.println(line);
            } catch (IOException e) {
                Output.println("Error reading file " + f.getName() + ": " + e.getMessage());
            }
        }
    }

    public void deleteMoodFile(String username) {
        File f = new File("moods_" + username + ".txt");
        if (f.exists()) {
            if (f.delete()) Output.println("Mood file for " + username + " deleted.");
            else Output.println("Failed to delete mood file for " + username);
        } else {
            Output.println("No mood file found for " + username);
        }
    }

    // ================== REMINDERS ==================
    public void viewAllReminders() {
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith("reminders_") && name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            Output.println("No reminders recorded.");
            return;
        }

        for (File f : files) {
            Output.println("\n--- " + f.getName() + " ---");
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) Output.println(line);
            } catch (IOException e) {
                Output.println("Error reading file " + f.getName() + ": " + e.getMessage());
            }
        }
    }

    public void deleteReminderFile(String username) {
        File f = new File("reminders_" + username + ".txt");
        if (f.exists()) {
            if (f.delete()) Output.println("Reminder file for " + username + " deleted.");
            else Output.println("Failed to delete reminder file for " + username);
        } else {
            Output.println("No reminder file found for " + username);
        }
    }

    // ================== TO-DO LIST ==================
    public void viewAllToDoLists() {
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith("todo_") && name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            Output.println("No To-Do list recorded.");
            return;
        }

        for (File f : files) {
            Output.println("\n--- " + f.getName() + " ---");
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) Output.println(line);
            } catch (IOException e) {
                Output.println("Error reading file " + f.getName() + ": " + e.getMessage());
            }
        }
    }

    public void deleteToDoFile(String username) {
        File f = new File("todo_" + username + ".txt");
        if (f.exists()) {
            if (f.delete()) Output.println("To-Do list file for " + username + " deleted.");
            else Output.println("Failed to delete To-Do list file for " + username);
        } else {
            Output.println("No To-Do list file found for " + username);
        }
    }

    // ================== QUOTES ==================
    public void viewAllQuotes() {
        File file = new File("quotes.txt");
        if (!file.exists()) {
            Output.println("No quotes available.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            Output.println("\n=== Quotes List ===");
            while ((line = br.readLine()) != null) Output.println(line);
        } catch (IOException e) {
            Output.println("Error reading quotes file: " + e.getMessage());
        }
    }

    // ================== GAMES ==================
    public void viewAllGuessGameStats() {
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith("guess_") && name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            Output.println("No Guess Game records found.");
            return;
        }

        for (File f : files) {
            Output.println("\n--- " + f.getName() + " ---");
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) Output.println(line);
            } catch (IOException e) {
                Output.println("Error reading file " + f.getName() + ": " + e.getMessage());
            }
        }
    }

    public void viewAllTriviaGameStats() {
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith("trivia_") && name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            Output.println("No Trivia Game records found.");
            return;
        }

        for (File f : files) {
            Output.println("\n--- " + f.getName() + " ---");
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) Output.println(line);
            } catch (IOException e) {
                Output.println("Error reading file " + f.getName() + ": " + e.getMessage());
            }
        }
    }

    // ================== DELETE GAME RECORDS ==================
    public void deleteGuessFile(String username) {
        File f = new File("guess_" + username + ".txt");
        if (f.exists()) {
            if (f.delete()) Output.println("Guess game file for " + username + " deleted.");
            else Output.println("Failed to delete guess game file for " + username);
        } else {
            Output.println("No guess game file found for " + username);
        }
    }

    public void deleteTriviaFile(String username) {
        File f = new File("trivia_" + username + ".txt");
        if (f.exists()) {
            if (f.delete()) Output.println("Trivia game file for " + username + " deleted.");
            else Output.println("Failed to delete trivia game file for " + username);
        } else {
            Output.println("No trivia game file found for " + username);
        }
    }
}
