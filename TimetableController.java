package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import java.util.List;
import java.util.Optional;

public class TimetableController {

    @FXML private GridPane timetableGrid;
    @FXML private ColorPicker colorPicker;
    @FXML private TextField timeSlotInput;
    @FXML private TextField taskToEditInput;
    @FXML private Label statusLabel;
    @FXML private Button addUpdateButton;
    @FXML private Button addColorButton;
    @FXML private Button updateTimeButton;
    @FXML private Button deleteRowButton;

    private TimetableManager timetableManager;
    private final String[] DAY_HEADERS = {"Time", "MON", "TUES", "WED", "THU", "FRI", "SAT", "SUN"};
    private int selectedRow = -1;
    private int selectedCol = -1;


    public void initData(User user) {
        this.timetableManager = new TimetableManager(user.getUsername());
        colorPicker.setValue(Color.LIGHTBLUE);
        initializeGrid();
        loadTimetable();
        updateButtonVisibility(false);
    }

    private void updateButtonVisibility(boolean isTimeSlotSelected) {
        boolean isTaskSelected = selectedRow != -1 && selectedCol >= 1;

        addUpdateButton.setDisable(!isTaskSelected);
        addColorButton.setDisable(!isTaskSelected);

        updateTimeButton.setVisible(isTimeSlotSelected);
        updateTimeButton.setDisable(!isTimeSlotSelected);

        deleteRowButton.setVisible(isTimeSlotSelected);
        deleteRowButton.setDisable(!isTimeSlotSelected);

        if (!isTaskSelected && !isTimeSlotSelected) {
            taskToEditInput.clear();
            timeSlotInput.clear();
        }
    }


    private void initializeGrid() {
        timetableGrid.getChildren().clear();
        timetableGrid.getRowConstraints().clear();
        timetableGrid.getColumnConstraints().clear();

        ColumnConstraints timeCol = new ColumnConstraints();
        timeCol.setPercentWidth(15);
        timetableGrid.getColumnConstraints().add(timeCol);

        for (int i = 0; i < 7; i++) {
            ColumnConstraints dayCol = new ColumnConstraints();
            dayCol.setPercentWidth(85.0 / 7);
            timetableGrid.getColumnConstraints().add(dayCol);
        }

        RowConstraints headerRow = new RowConstraints();
        headerRow.setVgrow(Priority.NEVER);
        timetableGrid.getRowConstraints().add(headerRow);

        for (int i = 0; i < DAY_HEADERS.length; i++) {
            Label header = createHeaderLabel(DAY_HEADERS[i]);
            timetableGrid.add(header, i, 0);
        }
    }

    private Label createHeaderLabel(String text) {
        Label label = new Label(text);
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        label.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-padding: 8; -fx-background-color: #CCCCFF; -fx-border-color: #5A5AEB; -fx-border-width: 0 0 1 1;");
        return label;
    }

    private StackPane createCell(int col, int row, TimetableEntry entry, boolean isTimeSlot) {
        if (entry == null) entry = new TimetableEntry("N/A", "#CCCCCC");

        Label label = new Label(entry.getTask());
        label.setStyle("-fx-alignment: center; -fx-text-fill: black;");
        label.setWrapText(true);

        StackPane pane = new StackPane(label);
        pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        if (isTimeSlot) {
            pane.setStyle("-fx-background-color: #E6E6FF; -fx-border-color: #5A5AEB; -fx-border-width: 0 1 1 0;");
        } else {
            String colorStyle = "-fx-background-color: " + entry.getColorHex() + ";";
            pane.setStyle(colorStyle + "-fx-border-color: #D3D3D3; -fx-border-width: 0 1 1 0;");

            pane.setOnMouseClicked(e -> handleCellClick(e, row, col));
        }

        return pane;
    }

    private void loadTimetable() {
        timetableGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
        if (timetableGrid.getRowConstraints().size() > 1) {
            timetableGrid.getRowConstraints().remove(1, timetableGrid.getRowConstraints().size());
        }

        List<List<TimetableEntry>> data = timetableManager.getTimetableData();
        List<String> timeSlots = timetableManager.getTimeSlots();

        for (int row = 0; row < data.size(); row++) {
            int gridRow = row + 1;

            RowConstraints dataRow = new RowConstraints();
            dataRow.setVgrow(Priority.ALWAYS);
            dataRow.setMinHeight(30);
            timetableGrid.getRowConstraints().add(dataRow);

            TimetableEntry timeEntry = new TimetableEntry(timeSlots.get(row), "#E6E6FF");
            StackPane timePane = createCell(0, gridRow, timeEntry, true);


            timePane.setOnMouseClicked(e -> handleTimeSlotClick(e, gridRow, 0));

            timetableGrid.add(timePane, 0, gridRow);

            List<TimetableEntry> rowData = data.get(row);
            for (int col = 0; col < rowData.size(); col++) {
                timetableGrid.add(createCell(col + 1, gridRow, rowData.get(col), false), col + 1, gridRow);
            }
        }
    }

    // --- Action Handlers (Input/Navigation) ---

    @FXML
    private void handleAddRow(ActionEvent event) {
        String timeSlot = timeSlotInput.getText().trim();
        if (timeSlot.isEmpty()) {
            statusLabel.setText("Please enter a time slot before adding a row.");
            return;
        }

        timetableManager.addRow(timeSlot);
        loadTimetable();
        timeSlotInput.clear();
        statusLabel.setText("New time slot added: " + timeSlot);
    }

    // Handler for TASKS (Col 1-7)
    @FXML
    private void handleCellClick(MouseEvent event, int row, int col) {
        // 1. Unhighlight previous selection (if any)
        if (selectedRow != -1 && selectedCol != -1) {
            StackPane prevPane = (StackPane) getNodeByRowColumnIndex(selectedRow, selectedCol, timetableGrid);
            if (prevPane != null) {
                timetableGrid.getChildren().remove(prevPane);

                TimetableEntry entry = timetableManager.getEntry(selectedRow - 1, selectedCol);
                boolean wasTimeSlot = (selectedCol == 0);

                // Reload the previous cell (no highlight)
                timetableGrid.add(createCell(selectedCol, selectedRow, entry, wasTimeSlot), selectedCol, selectedRow);
            }
        }

        // 2. Setup new selection
        selectedRow = row;
        selectedCol = col;

        StackPane selectedPane = (StackPane) event.getSource();
        selectedPane.setStyle(selectedPane.getStyle() + "-fx-border-color: red; -fx-border-width: 3;");

        // 3. Update Inputs
        TimetableEntry currentEntry = timetableManager.getEntry(row - 1, col);

        if (currentEntry == null) {
            statusLabel.setText("Error: Data index mismatch. Please restart.");
            return;
        }

        taskToEditInput.setText(currentEntry.getTask());
        colorPicker.setValue(Color.web(currentEntry.getColorHex()));

        statusLabel.setText("Selected task cell (Row: " + row + ", Day: " + DAY_HEADERS[col] + "). Type new task/color and APPLY.");

        // NEW: Update visibility - Task cell selected (Col 1-7)
        updateButtonVisibility(false);
    }

    // Handler for TIME SLOTS (Col 0)
    private void handleTimeSlotClick(MouseEvent event, int row, int col) {
        // 1. Unhighlight previous selection (if needed, same logic as handleCellClick)

        selectedRow = row;
        selectedCol = col; // Must be 0

        StackPane selectedPane = (StackPane) event.getSource();
        selectedPane.setStyle(selectedPane.getStyle() + "-fx-border-color: red; -fx-border-width: 3;");

        // 2. Load the current time slot text into the time slot input field
        timeSlotInput.setText(timetableManager.getTimeSlots().get(row - 1));
        taskToEditInput.clear();

        statusLabel.setText("Selected Time Slot (Row: " + row + "). Use 'Update Time' or 'Delete Row' buttons.");

        // NEW: Update visibility - Time Slot cell selected (Col 0)
        updateButtonVisibility(true);
    }

    // =======================================================
    // === NEW GRANULAR ACTION HANDLERS ===
    // =======================================================

    // --- New: Handler for Add/Update Task ---
    @FXML
    private void handleAddUpdateTask(ActionEvent event) {
        if (selectedRow <= 0 || selectedCol < 1) {
            statusLabel.setText("Please select a task cell (Mon-Sun) first.");
            return;
        }

        String newTask = taskToEditInput.getText().trim();
        if (newTask.isEmpty()) {
            statusLabel.setText("Task cannot be empty. Please type the task first.");
            return;
        }

        // Get current color (so we only change the task)
        TimetableEntry currentEntry = timetableManager.getEntry(selectedRow - 1, selectedCol);
        String currentHex = (currentEntry != null) ? currentEntry.getColorHex() : "#FFFFFF";

        // Update model (selectedCol is 1-7)
        timetableManager.updateEntry(selectedRow - 1, selectedCol, newTask, currentHex);
        loadTimetable();

        statusLabel.setText("Task updated successfully for " + DAY_HEADERS[selectedCol] + ".");
    }

    // --- New: Handler for Add/Update Color ---
    @FXML
    private void handleAddUpdateColor(ActionEvent event) {
        if (selectedRow <= 0 || selectedCol < 1) {
            statusLabel.setText("Please select a task cell (Mon-Sun) first.");
            return;
        }

        // Get current task content and update only the color
        TimetableEntry currentEntry = timetableManager.getEntry(selectedRow - 1, selectedCol);
        if (currentEntry == null) {
            statusLabel.setText("Error retrieving cell data.");
            return;
        }

        String currentTask = currentEntry.getTask();
        String newColorHex = toRgbString(colorPicker.getValue());

        // Update model
        timetableManager.updateEntry(selectedRow - 1, selectedCol, currentTask, newColorHex);
        loadTimetable();

        statusLabel.setText("Color applied successfully.");
    }

    // --- New: Handler for Update Time Slot ---
    @FXML
    private void handleUpdateTimeSlot(ActionEvent event) {
        if (selectedRow <= 0 || selectedCol != 0) {
            statusLabel.setText("Please select a Time Slot cell (Column 0) to update the time.");
            return;
        }

        String newTimeSlot = timeSlotInput.getText().trim();
        if (newTimeSlot.isEmpty()) {
            statusLabel.setText("Time slot cannot be empty.");
            return;
        }

        timetableManager.updateTimeSlot(selectedRow - 1, newTimeSlot);
        loadTimetable();

        statusLabel.setText("Time slot updated successfully to: " + newTimeSlot);
    }

    // --- New: Handler for Delete Row ---
    @FXML
    private void handleDeleteRow(ActionEvent event) {
        if (selectedRow <= 0 || selectedCol != 0) {
            statusLabel.setText("Please select a Time Slot cell to delete the entire row.");
            return;
        }

        // Confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this time slot row?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Confirm Row Deletion");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            String deletedTime = timetableManager.deleteRow(selectedRow - 1); // Assumes new method in Manager
            loadTimetable();
            statusLabel.setText("Row for time slot '" + deletedTime + "' successfully deleted.");
            updateButtonVisibility(false); // Hide buttons after deletion
        } else {
            statusLabel.setText("Deletion cancelled.");
        }
    }

    // --- New: Handler for Reset Timetable ---
    @FXML
    private void handleResetTimetable(ActionEvent event) {
        // Confirmation dialog is HIGHLY recommended here!
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete ALL timetable data? This cannot be undone.", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Confirm Timetable Reset");
        if (alert.showAndWait().filter(r -> r == ButtonType.YES).isPresent()) {
            timetableManager.resetTimetable(); // Assumes new method in Manager
            loadTimetable();
            statusLabel.setText("Timetable completely reset.");
            updateButtonVisibility(false);
        }
    }

    // --- Utility Methods ---

    // Convert Color object to hex string
    private String toRgbString(Color c) {
        return String.format("#%02X%02X%02X",
                (int)(c.getRed() * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue() * 255));
    }

    // Helper to find node in GridPane by index
    private javafx.scene.Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);

            if (rowIndex != null && colIndex != null && rowIndex == row && colIndex == column) {
                return node;
            }
        }
        return null;
    }
}
