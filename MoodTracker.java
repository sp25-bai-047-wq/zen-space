package com.example.backup;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MoodTracker {

    private final String username;
    private final String fileName;

    public MoodTracker(String username) {
        this.username = username;
        this.fileName = "moods_" + username + ".txt";
    }

    public boolean hasLoggedToday() {
        LocalDate today = LocalDate.now();
        File file = new File(fileName);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(today.toString() + "::")) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading mood file: " + e.getMessage());
        }
        return false;
    }

    public List<String> getTodaysMoods() {
        LocalDate today = LocalDate.now();
        File file = new File(fileName);
        if (!file.exists()) return List.of();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(today.toString() + "::")) {
                    String moodsPart = line.substring(line.indexOf("::") + 2);
                    return List.of(moodsPart.split(","));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading mood file for retrieval: " + e.getMessage());
        }
        return List.of();
    }

    public boolean saveMood(List<String> moods) {
        if (moods.isEmpty()) return false;

        LocalDate today = LocalDate.now();
        String entry = today.toString() + "::" + moods.stream().collect(Collectors.joining(","));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(entry + "\n");
            return true;
        } catch (IOException e) {
            System.err.println("Error saving mood entry: " + e.getMessage());
            return false;
        }
    }
}
