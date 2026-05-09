package ProjectPhase1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;
import java.util.ArrayList;
class DiaryEntry {
    private final String title;
    private final String content;
    private String formattedDate;

    public DiaryEntry(String title, String content, String dateInput) {
        this.title = title;
        this.content = content;
        setFormattedDate(dateInput);
    }

    private void setFormattedDate(String dateInput) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        inputFormat.setLenient(false);

        try {
            Date date = inputFormat.parse(dateInput);
            this.formattedDate = inputFormat.format(date);
        } catch (Exception e) {
            Output.println("Invalid date! Using today's date instead.");
            Date today = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            this.formattedDate = formatter.format(today);
        }
    }

    public String displayEntry() {
        return "Date: " + formattedDate + "\nTitle: " + title + "\nContent: " + content;
    }

    // --- Add getters for file saving ---
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getFormattedDate() { return formattedDate; }
}

class Diary {
    private final ArrayList<DiaryEntry> entries;
    private final String diaryFilePath;

    public Diary(String username) {
        entries = new ArrayList<>();
        diaryFilePath = username + "_diary.txt"; // one file per user
        loadEntriesFromFile();
    }

    // Add a new entry
    public void addEntry(DiaryEntry entry) {
        entries.add(entry);
        saveEntryToFile(entry);
        Output.println("Entry added and saved successfully!");
    }

    // View all entries
    public void viewEntries() {
        if (entries.isEmpty()) {
            Output.println("No diary entries yet!");
        } else {
            for (DiaryEntry e : entries) {
                Output.println(e.displayEntry());
                Output.println("-----------------------");
            }
        }
    }

    // Number of entries
    public int getCount() {
        return entries.size();
    }

    // Save a single entry to file
    private void saveEntryToFile(DiaryEntry entry) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(diaryFilePath, true))) {
            // Save as: date|title|content
            pw.println(entry.getFormattedDate() + "|" + entry.getTitle() + "|" + entry.getContent());
        } catch (IOException e) {
            Output.println("Error saving diary entry: " + e.getMessage());
        }
    }

    // Load all entries from file on startup
    private void loadEntriesFromFile() {
        File f = new File(diaryFilePath);
        if (!f.exists()) return; // no file yet

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 3);
                if (parts.length == 3) {
                    entries.add(new DiaryEntry(parts[1], parts[2], parts[0]));
                }
            }
        } catch (IOException e) {
            Output.println("Error loading diary entries: " + e.getMessage());
        }
    }
}
