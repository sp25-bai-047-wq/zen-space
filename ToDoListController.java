package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.util.List;

public class ToDoListController {

    // === FXML Elements ===
    @FXML private ListView<TaskItem> tasksListView;
    @FXML private TextField newTaskInput;
    @FXML private Label addFeedbackLabel;
    @FXML private Label listFeedbackLabel;

    // === Models and State ===
    private User loggedInUser;
    private ToDoListManager toDoListModel;

    // --- Initialization ---
    public void initData(User user) {
        this.loggedInUser = user;
        // Instantiate the model using the logged-in user's data
        this.toDoListModel = new ToDoListManager(user.getUsername());

        setupTaskListCellFactory();
        refreshTaskList();
    }

    private void setupTaskListCellFactory() {
        tasksListView.setCellFactory(lv -> new ListCell<TaskItem>() {
            private final CheckBox checkBox = new CheckBox();
            private final HBox graphic = new HBox(10, checkBox);

            @Override
            protected void updateItem(TaskItem item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    // Set up the checkbox and description
                    checkBox.setText(item.getDescription());
                    checkBox.setSelected(item.completeProperty().get());

                    // CRITICAL: Listener to save state immediately when checkbox is clicked
                    checkBox.setOnAction(e -> handleCheckboxToggle(item));

                    setGraphic(graphic);
                    setText(null);
                }
            }
        });
    }

    private void handleCheckboxToggle(TaskItem item) {
        // The Model uses a 1-based index (index stored in TaskItem is 0-based)
        int taskNumber = item.getIndex() + 1;

        // Call the Model's logic to toggle and save the state
        String statusMessage = toDoListModel.markComplete(taskNumber);

        listFeedbackLabel.setText(statusMessage);

        // Refresh the list to reflect the new state from the Model's accessor lists
        refreshTaskList();
    }

    /**
     * Fetches the current task list from the Model's internal state and updates the ListView.
     */
    private void refreshTaskList() {
        // Retrieve raw data using the new accessor methods from the Model
        List<String> descriptions = toDoListModel.getTaskDescriptions();
        List<Boolean> statuses = toDoListModel.getTaskCompletionStatus();

        ObservableList<TaskItem> taskItems = FXCollections.observableArrayList();

        // Iterate through the parallel lists to create TaskItem objects
        for (int i = 0; i < descriptions.size(); i++) {
            taskItems.add(new TaskItem(descriptions.get(i), statuses.get(i), i));
        }

        tasksListView.setItems(taskItems);

        if (taskItems.isEmpty()) {
            listFeedbackLabel.setText("You have no tasks! Time to relax or add one.");
        } else {
            listFeedbackLabel.setText("");
        }
    }

    // --- Action Handlers ---

    @FXML
    private void handleAddTask(ActionEvent event) {
        String taskDescription = newTaskInput.getText().trim();

        if (taskDescription.isEmpty()) {
            addFeedbackLabel.setText("Task cannot be empty!");
            return;
        }

        String statusMessage = toDoListModel.addTask(taskDescription);

        addFeedbackLabel.setText(statusMessage);
        newTaskInput.clear();
        refreshTaskList();
    }

    @FXML
    private void handleRemoveTask(ActionEvent event) {
        TaskItem selectedItem = tasksListView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            listFeedbackLabel.setText("Please select a task to remove.");
            return;
        }

        // The Model uses a 1-based index (taskNumber = index stored in item + 1)
        int taskNumber = selectedItem.getIndex() + 1;
        String statusMessage = toDoListModel.removeTask(taskNumber);

        listFeedbackLabel.setText(statusMessage);
        refreshTaskList();
    }
}