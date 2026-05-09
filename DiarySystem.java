package com.example.backup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class DiaryEntry {
    private final String title;
    private final String content;
    private String formattedDate;
    private final String fontName;

    public DiaryEntry(String title, String content, String dateInput, String fontName) {
        this.title = title;
        this.content = content;
        this.fontName = fontName;
        setFormattedDate(dateInput);
    }

    private void setFormattedDate(String dateInput) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        inputFormat.setLenient(false);

        try {
            Date date = inputFormat.parse(dateInput);
            this.formattedDate = inputFormat.format(date);
        } catch (Exception e) {
            System.err.println("Invalid date in file! Using today's date instead for entry: " + this.title);
            Date today = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            this.formattedDate = formatter.format(today);
        }
    }

    public String displayEntry() {
        return "Date: " + formattedDate + "\nTitle: " + title + "\nContent: " + content;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getFormattedDate() { return formattedDate; }
    public String getFontName() { return fontName; }
}

class Diary{

    private final String username;

    public Diary(String username) {
        this.username = username;
    }

    private String getFileName() {
        return "diary_" + username + ".txt";
    }

    public boolean addEntry(DiaryEntry entry) {
        String entryString = entry.getTitle() + "::" +
                entry.getContent() + "::" +
                entry.getFormattedDate() + "::" +
                entry.getFontName();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFileName(), true))) {
            bw.write(entryString + "\n");
            return true;
        } catch (IOException e) {
            System.err.println("Error saving diary entry: " + e.getMessage());
            return false;
        }
    }

    public List<DiaryEntry> getEntries() {
        List<DiaryEntry> entries = new ArrayList<>();
        File file = new File(getFileName());
        if (!file.exists()) return entries;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("::", 4);

                if (parts.length == 4) {
                    entries.add(new DiaryEntry(parts[0], parts[1], parts[2], parts[3]));
                } else if (parts.length == 3) {
                    entries.add(new DiaryEntry(parts[0], parts[1], parts[2], "System"));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading diary entries: " + e.getMessage());
        }
        return entries;
    }

    public boolean deleteEntry(DiaryEntry entryToDelete) {
        List<DiaryEntry> allEntries = getEntries();
        boolean removed = allEntries.removeIf(entry ->
                entry.getTitle().equals(entryToDelete.getTitle()) &&
                        entry.getContent().equals(entryToDelete.getContent()) &&
                        entry.getFormattedDate().equals(entryToDelete.getFormattedDate())
        );

        if (removed) {
            return rewriteFile(allEntries);
        }
        return false;
    }

    private boolean rewriteFile(List<DiaryEntry> entries) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFileName(), false))) {
            for (DiaryEntry entry : entries) {
                String entryString = entry.getTitle() + "::" +
                        entry.getContent() + "::" +
                        entry.getFormattedDate() + "::" +
                        entry.getFontName();
                bw.write(entryString + "\n");
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error rewriting diary file: " + e.getMessage());
            return false;
        }
    }
}