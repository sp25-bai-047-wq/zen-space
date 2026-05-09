package com.example.backup;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TaskItem {
    private final StringProperty description;
    private final BooleanProperty complete;
    private final int index; // Stores the 0-based index of the task in the Model list

    public TaskItem(String description, boolean complete, int index) {
        this.description = new SimpleStringProperty(description);
        this.complete = new SimpleBooleanProperty(complete);
        this.index = index;
    }

    public String getDescription() { return description.get(); }
    public BooleanProperty completeProperty() { return complete; }
    public int getIndex() { return index; }

    @Override
    public String toString() {
        return description.get();
    }
}