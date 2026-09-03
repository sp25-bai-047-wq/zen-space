package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainController {

    @FXML private Label userLabel;
    @FXML private VBox contentArea;
    @FXML private BorderPane rootPane;

    private User loggedInUser;
    private ZenSpace mainApp;


    private Map<String, Boolean> entryUnlockStatus = new HashMap<>();


    public void initData(User user, ZenSpace app) {
        this.loggedInUser = user;
        this.mainApp = app;

        userLabel.setText("Welcome, " + loggedInUser.getUsername());

    }


    public void loadDiaryGameView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DiaryGameView.fxml"));
            Parent view = loader.load();

            DiaryGameController gameController = loader.getController();
            gameController.initData(loggedInUser, this);

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Error loading DiaryGameView.fxml: " + e.getMessage());
            contentArea.getChildren().setAll(new Label("ERROR LOADING GAME VIEW."));
        }
    }

    public boolean isEntryUnlockedForUser(User user) {
        return entryUnlockStatus.getOrDefault(user.getUsername(), false);
    }

    public void setEntryUnlockedForUser(User user, boolean status) {
        entryUnlockStatus.put(user.getUsername(), status);
    }

    public void loadDiaryEntriesView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DiaryEntriesView.fxml"));
            Parent view = loader.load();

            DiaryEntriesController entriesController = loader.getController();
            entriesController.initData(loggedInUser, this);

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Error loading DiaryEntriesView.fxml: " + e.getMessage());
            contentArea.getChildren().setAll(new Label("ERROR LOADING ENTRIES VIEW."));
        }
    }

    public void loadToDoListView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ToDoListView.fxml"));
            Parent view = loader.load();

            ToDoListController controller = loader.getController();
            controller.initData(loggedInUser);

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Error loading ToDoListView.fxml: " + e.getMessage());
            contentArea.getChildren().setAll(new Label("ERROR LOADING TO-DO LIST."));
        }
    }

    public void loadTimetableView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TimetableNavigationView.fxml"));
            Parent view = loader.load();

            TimetableController controller = loader.getController();
            controller.initData(loggedInUser);

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Error loading TimetableNavigationView.fxml: " + e.getMessage());
            contentArea.getChildren().setAll(new Label("ERROR LOADING TIMETABLE."));
        }
    }

    public void loadMoodTrackerView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MoodTrackerView.fxml"));
            Parent view = loader.load();
            MoodTrackerController controller = loader.getController();
            controller.initData(loggedInUser, this);

            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRandomQuoteView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RandomQuoteView.fxml"));
            Parent view = loader.load();

            RandomQuoteController controller = loader.getController();
            controller.initData(loggedInUser, this);

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Error loading RandomQuoteView.fxml: " + e.getMessage());
            contentArea.getChildren().setAll(new Label("ERROR LOADING RANDOM QUOTE VIEW."));
        }
    }

    public void loadAddViewQuoteView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddViewQuoteView.fxml"));
            Parent view = loader.load();

            AddViewQuoteController controller = loader.getController();
            controller.initData(loggedInUser, this);

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Error loading AddViewQuoteView.fxml: " + e.getMessage());
            contentArea.getChildren().setAll(new Label("ERROR LOADING ADD/VIEW QUOTES."));
        }
    }

    public void loadFunZoneView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FunZoneView.fxml"));
            Parent view = loader.load();

            FunZoneController controller = loader.getController();
            controller.initData(loggedInUser, this);

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Error loading FunZoneView.fxml: " + e.getMessage());
            contentArea.getChildren().setAll(new Label("ERROR LOADING FUN ZONE."));
        }
    }

    public void loadGuessGameView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GuessGameView.fxml"));
            Parent view = loader.load();

            GuessGameController controller = loader.getController();
            controller.initData(loggedInUser, this);

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Error loading GuessGameView.fxml: " + e.getMessage());
            contentArea.getChildren().setAll(new Label("ERROR LOADING GUESS GAME."));
        }
    }

    public void loadTriviaGameView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TriviaGameView.fxml"));
            Parent view = loader.load();

            TriviaGameController controller = loader.getController();
            controller.initData(loggedInUser, this);

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Error loading TriviaGameView.fxml: " + e.getMessage());
            contentArea.getChildren().setAll(new Label("ERROR LOADING TRIVIA GAME."));
        }
    }

    public void loadView(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent view = loader.load();

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlFileName);
            e.printStackTrace();
            contentArea.getChildren().setAll(new Label("ERROR LOADING VIEW: " + fxmlFileName));
        }
    }


    @FXML
    public void handleDiaryGameClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DiaryMenuView.fxml"));
            Parent view = loader.load();
            DiaryMenuController menuController = loader.getController();
            menuController.initData(loggedInUser, this);

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Error loading view: DiaryMenuView.fxml");
            e.printStackTrace();
            contentArea.getChildren().setAll(new Label("ERROR LOADING DIARY MENU."));
        }
    }

    @FXML
    public void handleToDoListClick() {
        loadToDoListView();
    }

    @FXML
    private void handleRemindersClick() {

        loadTimetableView();
    }


    @FXML
    public void handleMoodTrackerClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MoodMenuView.fxml"));
            Parent view = loader.load();
            MoodMenuController controller = loader.getController();
            controller.initData(loggedInUser, this);
            contentArea.getChildren().setAll(view);
            VBox.setVgrow(view, javafx.scene.layout.Priority.ALWAYS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMoodHistoryView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MoodHistoryView.fxml"));
            Parent view = loader.load();
            MoodHistoryController controller = loader.getController();
            controller.initData(loggedInUser, this);
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleQuotesClick() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("QuoteMenuView.fxml"));
            Parent view = loader.load();

            QuoteMenuController menuController = loader.getController();
            menuController.initData(loggedInUser, this);

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Error loading view: QuoteMenuView.fxml");
            e.printStackTrace();
            contentArea.getChildren().setAll(new Label("ERROR LOADING QUOTE MENU."));
        }
    }

    @FXML
    public void handleFunZoneClick() {
        loadFunZoneView();
    }

    @FXML
    public void handleLogoutClick() {
        if (mainApp != null) {
            mainApp.loadLoginView();
        }
    }

    @FXML
    public void initialize() {
    }
}
