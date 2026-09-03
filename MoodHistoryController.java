package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MoodHistoryController {
    @FXML private ListView<String> moodListView;
    private User loggedInUser;
    private MainController mainController;

    public void initData(User user, MainController controller) {
        this.loggedInUser = user;
        this.mainController = controller;
        loadMoodData();
    }

    private void loadMoodData() {
        String fileName = "moods_" + loggedInUser.getUsername() + ".txt";
        File file = new File(fileName);
        if (!file.exists()) {
            moodListView.getItems().add("No moods logged yet!");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String formatted = line.replace("::", ": ").replace(",", ", ");
                moodListView.getItems().add(0, formatted);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        mainController.handleMoodTrackerClick();
    }
}
