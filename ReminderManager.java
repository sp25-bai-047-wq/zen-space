package ProjectPhase1;

import java.io.*;
import java.util.*;

public class ReminderManager {

    private final String username;
    private final List<String> reminders;
    private final List<String> dates;

    public ReminderManager(String username) {
        this.username = username;
        reminders = new ArrayList<>();
        dates = new ArrayList<>();
        loadReminders();
    }

    private String getFileName() {
        return "reminders_" + username + ".txt";
    }

    // Load reminders from user's file
    private void loadReminders() {
        File file = new File(getFileName());
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("::", 2);
                if (parts.length == 2) {
                    dates.add(parts[0]);
                    reminders.add(parts[1]);
                }
            }
        } catch (IOException e) {
            Output.println("Error loading reminders for " + username + ": " + e.getMessage());
        }
    }

    // Save reminders to user's file
    private void saveReminders() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFileName()))) {
            for (int i = 0; i < reminders.size(); i++) {
                bw.write(dates.get(i) + "::" + reminders.get(i) + "\n");
            }
        } catch (IOException e) {
            Output.println("Error saving reminders for " + username + ": " + e.getMessage());
        }
    }

    // --- Normal user operations ---
    public boolean addReminder(String date, String message) {
        if (reminders.size() >= 5) {
            Output.println("Reminder list is full! Cannot add more.");
            return false;
        }
        dates.add(date);
        reminders.add(message);
        saveReminders();
        Output.println("Reminder added successfully: [" + date + "] - " + message);
        return true;
    }

    public boolean disableReminder(int index) {
        int arrayIndex = index - 1;
        if (arrayIndex >= 0 && arrayIndex < reminders.size()) {
            String removedMessage = reminders.remove(arrayIndex);
            String removedDate = dates.remove(arrayIndex);
            saveReminders();
            Output.println("Reminder disabled successfully: [" + removedDate + "] - " + removedMessage);
            return true;
        } else {
            Output.println("Invalid reminder number: " + index);
            return false;
        }
    }

    public void viewReminders() {
        Output.println("\n=== Your Reminders (" + reminders.size() + ") ===");
        if (reminders.isEmpty()) {
            Output.println("No reminders found!");
            return;
        }
        for (int i = 0; i < reminders.size(); i++) {
            Output.println((i + 1) + ". [" + dates.get(i) + "] - " + reminders.get(i));
        }
    }

    // --- Admin operations ---
    public static void viewAllUserReminders(String user) {
        File file = new File("reminders_" + user + ".txt");
        if (!file.exists()) {
            Output.println("No reminders found for user: " + user);
            return;
        }
        Output.println("\n=== Reminders for user: " + user + " ===");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 1;
            while ((line = br.readLine()) != null) {
                Output.println(count + ". " + line.replace("::", " - "));
                count++;
            }
        } catch (IOException e) {
            Output.println("Error reading reminders for user " + user + ": " + e.getMessage());
        }
    }

    public static boolean deleteUserReminder(String user, int index) {
        File file = new File("reminders_" + user + ".txt");
        if (!file.exists()) {
            Output.println("No reminders found for user: " + user);
            return false;
        }
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
        } catch (IOException e) {
            Output.println("Error reading reminders: " + e.getMessage());
            return false;
        }

        int arrayIndex = index - 1;
        if (arrayIndex < 0 || arrayIndex >= lines.size()) {
            Output.println("Invalid reminder number for user: " + user);
            return false;
        }

        lines.remove(arrayIndex);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String l : lines) bw.write(l + "\n");
        } catch (IOException e) {
            Output.println("Error saving reminders: " + e.getMessage());
            return false;
        }

        Output.println("Reminder deleted successfully for user: " + user);
        return true;
    }
}
