package com.example.backup;

import javafx.scene.paint.Color;

public class TimetableEntry {
    private String task;
    private String colorHex;

    public TimetableEntry(String task, String colorHex) {
        this.task = task;
        this.colorHex = colorHex;
    }

    public TimetableEntry() {
        this("", "#FFFFFF");
    }

    public String getTask() { return task; }
    public String getColorHex() { return colorHex; }

    public void setTask(String task) { this.task = task; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    @Override
    public String toString() {
        return task + "|" + colorHex;
    }

    public static TimetableEntry fromString(String data) {
        if (data == null || data.trim().isEmpty() || !data.contains("|")) {
            return new TimetableEntry();
        }
        String[] parts = data.split("\\|", 2);
        String task = parts[0];
        String colorHex = (parts.length > 1) ? parts[1] : "#FFFFFF";
        return new TimetableEntry(task, colorHex);
    }
}
