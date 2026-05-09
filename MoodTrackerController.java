package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MoodTrackerController {

    @FXML private TilePane moodTilePane;
    @FXML private Label selectedMoodsLabel;
    @FXML private Label statusLabel;

    private User loggedInUser;
    private MoodTracker moodTrackerModel;
    private MainController mainController;

    private final List<String> selectedMoods = new ArrayList<>();
    private final List<Button> moodButtons = new ArrayList<>();

    private static final List<String> ALL_MOODS = List.of(
            "calm", "happy", "energetic", "frisky", "mood swings",
            "irritated", "sad", "anxious", "depressed", "feeling guilty",
            "obsessive thoughts", "low energy", "apathetic", "confused",
            "very self-critical", "stressed", "neutral"
    );

    private static final String STYLE_UNSELECTED = "-fx-background-color: #FFFFFF; -fx-border-color: #8e7f8f; -fx-text-fill: #8e7f8f;" ;
    private static final String STYLE_SELECTED = "-fx-background-color: #8e7f8f; -fx-border-color: #36368C; -fx-font-weight: bold; -fx-text-fill: white;";
    private static final String STYLE_DISABLED = "-fx-background-color: #E0E0E0; -fx-text-fill: #999999;";

    public void initData(User user, MainController controller) {
        this.loggedInUser = user;
        this.mainController = controller;
        this.moodTrackerModel = new MoodTracker(user.getUsername());

        if (moodTrackerModel.hasLoggedToday()) {
            statusLabel.setText("You have already logged your mood for " + LocalDate.now() + ".");
            selectedMoodsLabel.setText(moodTrackerModel.getTodaysMoods().stream().collect(Collectors.joining(", ")));
            createMoodButtons(true);
        } else {
            createMoodButtons(false);
        }
    }

    private void createMoodButtons(boolean disableAll) {
        moodTilePane.getChildren().clear();
        moodButtons.clear();

        for (String mood : ALL_MOODS) {
            Button btn = new Button(mood.substring(0, 1).toUpperCase() + mood.substring(1));
            btn.setPrefSize(140, 40);
            btn.setStyle(STYLE_UNSELECTED);
            btn.setOnAction(this::handleMoodButtonClick);
            btn.setDisable(disableAll);

            if (disableAll) {
                btn.setStyle(STYLE_DISABLED);
            }

            moodButtons.add(btn);
            moodTilePane.getChildren().add(btn);
        }
    }

    private void handleMoodButtonClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String mood = sourceButton.getText().toLowerCase();

        if (selectedMoods.contains(mood)) {
            selectedMoods.remove(mood);
            sourceButton.setStyle(STYLE_UNSELECTED);
        } else {
            selectedMoods.add(mood);
            sourceButton.setStyle(STYLE_SELECTED);
        }

        updateSelectedMoodsLabel();
    }

    private void updateSelectedMoodsLabel() {
        if (selectedMoods.isEmpty()) {
            selectedMoodsLabel.setText("No moods selected yet.");
        } else {
            String moodString = selectedMoods.stream()
                    .map(m -> m.substring(0, 1).toUpperCase() + m.substring(1))
                    .collect(Collectors.joining(", "));
            selectedMoodsLabel.setText(moodString);
        }
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        if (mainController != null) {
            mainController.handleMoodTrackerClick();
        }
    }

    @FXML
    private void handleSaveMood(ActionEvent event) {
        if (selectedMoods.isEmpty()) {
            statusLabel.setText("Please select at least one mood before saving.");
            return;
        }

        if (moodTrackerModel.hasLoggedToday()) {
            statusLabel.setText("Error: Mood already logged for today. Cannot log again.");
            return;
        }

        boolean success = moodTrackerModel.saveMood(selectedMoods);

        if (success) {
            statusLabel.setText("Moods saved successfully for " + LocalDate.now() + ". Selection is now locked.");

            for (Button btn : moodButtons) {
                btn.setDisable(true);
                if (!btn.getStyle().contains(STYLE_SELECTED)) {
                    btn.setStyle(STYLE_DISABLED);
                }
            }
        } else {
            statusLabel.setText("Error saving moods to file.");
        }
    }
}