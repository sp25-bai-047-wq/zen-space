package ProjectPhase1;

import java.io.*;
import java.util.*;

public class ToDoListManager {

    private final String username;
    private final List<String> tasks;
    private final List<Boolean> isComplete;

    public ToDoListManager(String username) {
        this.username = username;
        tasks = new ArrayList<>();
        isComplete = new ArrayList<>();
        loadTasks();
    }

    // File name for each user
    private String getFileName() {
        return "todo_" + username + ".txt";
    }

    // Load tasks from user's file
    private void loadTasks() {
        File file = new File(getFileName());
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("::", 2);
                if (parts.length == 2) {
                    tasks.add(parts[0]);
                    isComplete.add(parts[1].equals("1"));
                }
            }
        } catch (IOException e) {
            Output.println("Error loading tasks for user " + username + ": " + e.getMessage());
        }
    }

    // Save tasks to user's file
    private void saveTasks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFileName()))) {
            for (int i = 0; i < tasks.size(); i++) {
                bw.write(tasks.get(i) + "::" + (isComplete.get(i) ? "1" : "0") + "\n");
            }
        } catch (IOException e) {
            Output.println("Error saving tasks for user " + username + ": " + e.getMessage());
        }
    }

    // Add task
    public boolean addTask(String taskDescription) {
        if (taskDescription == null || taskDescription.trim().isEmpty()) {
            Output.println("Task cannot be empty.");
            return false;
        }
        tasks.add(taskDescription.trim());
        isComplete.add(false);
        saveTasks();
        Output.println("Task added: " + taskDescription);
        return true;
    }

    // Remove task
    public boolean removeTask(int taskNumber) {
        int index = taskNumber - 1;
        if (index >= 0 && index < tasks.size()) {
            String removedTask = tasks.remove(index);
            isComplete.remove(index);
            saveTasks();
            Output.println("Task removed: " + removedTask);
            return true;
        } else {
            Output.println("Invalid task number.");
            return false;
        }
    }

    // Mark task complete
    public boolean markComplete(int taskNumber) {
        int index = taskNumber - 1;
        if (index >= 0 && index < tasks.size()) {
            if (isComplete.get(index)) {
                Output.println("Task '" + tasks.get(index) + "' is already complete.");
                return false;
            } else {
                isComplete.set(index, true);
                saveTasks();
                Output.println("Task marked complete: " + tasks.get(index));
                return true;
            }
        } else {
            Output.println("Invalid task number.");
            return false;
        }
    }

    // View user's tasks
    public void viewTasks() {
        Output.println("\n=== " + username + "'s To-Do List (" + tasks.size() + ") ===");
        if (tasks.isEmpty()) {
            Output.println("No tasks recorded yet.");
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            String status = isComplete.get(i) ? "✅" : "❌";
            Output.println(status + " " + (i + 1) + ". " + tasks.get(i));
        }
    }

    // ------------------- ADMIN FEATURES -------------------

    // Admin can view all users' To-Do lists
    public static void viewAllUsersTasks() {
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith("todo_") && name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            Output.println("No user To-Do lists found.");
            return;
        }

        for (File f : files) {
            String username = f.getName().replace("todo_", "").replace(".txt", "");
            ToDoListManager userList = new ToDoListManager(username);
            userList.viewTasks();
        }
    }

    // Admin can remove a task from any user's list
    public static boolean removeTaskForUser(String username, int taskNumber) {
        ToDoListManager userList = new ToDoListManager(username);
        return userList.removeTask(taskNumber);
    }

    // Admin can mark task complete for any user
    public static boolean markCompleteForUser(String username, int taskNumber) {
        ToDoListManager userList = new ToDoListManager(username);
        return userList.markComplete(taskNumber);
    }

}
