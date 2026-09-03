package com.example.backup;

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

    private String getFileName() { return "todo_" + username + ".txt"; }

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
            System.err.println("Error loading tasks for user " + username + ": " + e.getMessage());
        }
    }

    private boolean saveTasks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFileName()))) {
            for (int i = 0; i < tasks.size(); i++) {
                bw.write(tasks.get(i) + "::" + (isComplete.get(i) ? "1" : "0") + "\n");
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving tasks for user " + username + ": " + e.getMessage());
            return false;
        }
    }

    public String addTask(String taskDescription) {
        if (taskDescription == null || taskDescription.trim().isEmpty()) return "Task cannot be empty.";

        tasks.add(taskDescription.trim());
        isComplete.add(false);
        if (saveTasks()) {
            return "Task added: " + taskDescription;
        } else {
            tasks.remove(tasks.size() - 1);
            isComplete.remove(isComplete.size() - 1);
            return "Error saving task to file.";
        }
    }

    public String removeTask(int taskNumber) {
        int index = taskNumber - 1;
        if (index >= 0 && index < tasks.size()) {
            String removedTask = tasks.remove(index);
            isComplete.remove(index);
            if (saveTasks()) {
                return "Task removed: " + removedTask;
            } else {
                return "Task removed in memory, but failed to update file.";
            }
        } else {
            return "Invalid task number.";
        }
    }

    public String markComplete(int taskNumber) {
        int index = taskNumber - 1;
        if (index >= 0 && index < tasks.size()) {
            // Toggle the status based on current state
            boolean currentState = isComplete.get(index);

            isComplete.set(index, !currentState);

            if (saveTasks()) {
                if (!currentState) {
                    return "Task marked complete: " + tasks.get(index);
                } else {
                    return "Task marked incomplete: " + tasks.get(index);
                }
            } else {
                isComplete.set(index, currentState); // Revert in memory
                return "Task status changed in memory, but failed to update file.";
            }
        } else {
            return "Invalid task number.";
        }
    }

    // --- ACCESSOR METHODS ADDED FOR JAVAFX CONTROLLER ---

    /** Returns the raw list of task descriptions (used by Controller to build TaskItem). */
    public List<String> getTaskDescriptions() {
        return this.tasks;
    }

    /** Returns the raw list of completion statuses (used by Controller to build TaskItem). */
    public List<Boolean> getTaskCompletionStatus() {
        return this.isComplete;
    }

    // NOTE: Old getTasks() method is redundant but kept for completeness
    // public List<String> getTasks() { ... }
}
