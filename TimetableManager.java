package com.example.backup;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimetableManager {

    private final String fileName;
    private final List<List<TimetableEntry>> timetableData;
    private final List<String> timeSlots;

    public TimetableManager(String username) {
        this.fileName = "timetable_" + username + ".txt";
        this.timetableData = new ArrayList<>();
        this.timeSlots = new ArrayList<>();
        loadTimetable();
    }

    private void loadTimetable() {
        File file = new File(fileName);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("::", 2);
                if (parts.length != 2) continue;

                timeSlots.add(parts[0]);

                List<TimetableEntry> row = new ArrayList<>();
                String[] entries = parts[1].split(",");
                for (String entryData : entries) {
                    row.add(TimetableEntry.fromString(entryData));
                }
                timetableData.add(row);
            }
        } catch (IOException e) {
            System.err.println("Error loading timetable: " + e.getMessage());
        }
    }

    public boolean saveTimetable() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < timetableData.size(); i++) {
                List<TimetableEntry> row = timetableData.get(i);
                String timeSlot = timeSlots.get(i);

                String entriesString = row.stream()
                        .map(TimetableEntry::toString)
                        .collect(Collectors.joining(","));

                bw.write(timeSlot + "::" + entriesString);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving timetable: " + e.getMessage());
            return false;
        }
    }


    public void addRow(String timeSlot) {
        if (timeSlot.trim().isEmpty()) return;

        timeSlots.add(timeSlot.trim());

        List<TimetableEntry> newRow = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            newRow.add(new TimetableEntry());
        }
        timetableData.add(newRow);
    }

    public TimetableEntry getEntry(int rowIndex, int dayColumnIndex) {
        if (rowIndex >= 0 && rowIndex < timetableData.size() &&
                dayColumnIndex >= 1 && dayColumnIndex <= 7) {

            return timetableData.get(rowIndex).get(dayColumnIndex - 1);
        }
        return null;
    }

    public void updateEntry(int rowIndex, int dayColumnIndex, String task, String colorHex) {
        TimetableEntry entry = getEntry(rowIndex, dayColumnIndex);
        if (entry != null) {
            entry.setTask(task);
            entry.setColorHex(colorHex);
            saveTimetable();
        }
    }

    public void updateTimeSlot(int rowIndex, String newTimeSlot) {
        if (rowIndex >= 0 && rowIndex < timeSlots.size()) {
            timeSlots.set(rowIndex, newTimeSlot);
            saveTimetable();
        }
    }
    public String deleteRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < timeSlots.size()) {
            String deletedTime = timeSlots.remove(rowIndex);
            timetableData.remove(rowIndex);
            saveTimetable();
            return deletedTime;
        }
        return "N/A";
    }

    public void resetTimetable() {
        timeSlots.clear();
        timetableData.clear();
        saveTimetable();
    }

    public List<List<TimetableEntry>> getTimetableData() {
        return timetableData;
    }

    public List<String> getTimeSlots() {
        return timeSlots;
    }

    public int getRowCount() {
        return timetableData.size();
    }
}
