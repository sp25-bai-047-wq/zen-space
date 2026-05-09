package ProjectPhase1;

import java.util.Scanner;

public class ZenSpace {

    // Runs the main diary app after login
    public static void runDiaryApp(User currentUser, User[] allUsers, int userCount) {
        Scanner sc = new Scanner(System.in);
        boolean adminFlag = currentUser instanceof AdminUser;
        Diary diary = new Diary(currentUser.getUsername());
        MoodTracker moodTracker = new MoodTracker(currentUser.getUsername(),adminFlag);
        ReminderManager reminderManager = new ReminderManager(currentUser.getUsername());
        QuoteGenerator quoteGen = new QuoteGenerator(adminFlag);
        FunZoneManager funZone = new FunZoneManager(currentUser.getUsername());
        ToDoListManager todoManager = new ToDoListManager(currentUser.getUsername());

        int choice;
        do {
            System.out.println("\n=== ZenSpace MAIN MENU ===");
            System.out.println("1. Diary (Play Game + Add/View Entries)");
            System.out.println("2. Mood Tracker");
            System.out.println("3. Reminders");
            System.out.println("4. Motivational Quotes");
            System.out.println("5. Fun Zone");
            System.out.println("6. To-Do List");
            if (currentUser instanceof AdminUser) {
                System.out.println("7. Admin Dashboard");
                System.out.println("8. Logout");
            } else {
                System.out.println("7. Logout");
            }
            System.out.print("Choose an option: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Enter a number!");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            if (currentUser instanceof AdminUser) {
                switch (choice) {
                    case 1 -> diarySection(sc, diary,currentUser);
                    case 2 -> moodSection(sc, moodTracker, currentUser, allUsers, userCount);
                    case 3 -> reminderSection(sc, reminderManager, currentUser, allUsers, userCount);
                    case 4 -> quoteSection(sc, quoteGen, currentUser);
                    case 5 -> funZoneSection(sc, funZone, currentUser);
                    case 6 -> todoSection(sc, todoManager, currentUser, allUsers, userCount);
                    case 7 -> adminDashboard(sc, allUsers, userCount, quoteGen);
                    case 8 -> System.out.println("Logging out... Bye, " + currentUser.getUsername() + "!");
                    default -> System.out.println("Invalid choice!");
                }
            } else {
                switch (choice) {
                    case 1 -> diarySection(sc, diary,currentUser);
                    case 2 -> moodSection(sc, moodTracker, currentUser, allUsers, userCount);
                    case 3 -> reminderSection(sc, reminderManager, currentUser, allUsers, userCount);
                    case 4 -> quoteSection(sc, quoteGen, currentUser);
                    case 5 -> funZoneSection(sc, funZone, currentUser);
                    case 6 -> todoSection(sc, todoManager, currentUser, allUsers, userCount);
                    case 7 -> System.out.println("Logging out... Bye, " + currentUser.getUsername() + "!");
                    default -> System.out.println("Invalid choice!");
                }
            }

        } while ((currentUser instanceof AdminUser && choice != 8) || (!(currentUser instanceof AdminUser) && choice != 7));
    }

    // ------------------- Diary Section -------------------
    private static void diarySection(Scanner sc, Diary diary, User currentUser) {
        int ch;
        do {
            System.out.println("\n=== DIARY MENU ===");
            System.out.println("1. Play Level + Add Entry");
            System.out.println("2. View Entries");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Enter a valid number!");
                sc.next();
            }
            ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> {
                    // Load last played level from file
                    int levelToPlay = GameManager.ProgressManager.loadProgress();

                    if (levelToPlay > GameManager.ALL_LEVEL_DATA.length) {
                        System.out.println("All word game levels complete!");
                    } else {
                        GameManager.Level currentLevel = GameManager.startLevel(levelToPlay);
                        boolean completed = false;

                        if (currentLevel != null) {
                            while (!currentLevel.isComplete()) {
                                currentLevel.displayProgress(levelToPlay);
                                System.out.print("Enter word: ");
                                String input = sc.nextLine();
                                int result = GameManager.processInput(currentLevel, input);

                                if (result == -1) {
                                    System.out.println("Game aborted. Level not completed.");
                                    break;
                                }

                                if (currentLevel.isComplete()) {
                                    System.out.printf("Level %d COMPLETED! Diary entry unlocked.%n", levelToPlay);
                                    completed = true;
                                }
                            }
                        }

                        if (completed) {
                            // Save progress automatically
                            GameManager.ProgressManager.saveProgress(levelToPlay + 1);

                            System.out.println("\n--- Add New Diary Entry ---");
                            System.out.print("Enter date (dd-MM-yyyy): ");
                            String date = sc.nextLine();
                            System.out.print("Enter title: ");
                            String title = sc.nextLine();
                            System.out.print("Enter content: ");
                            String content = sc.nextLine();

                            // Add file-backed entry
                            diary.addEntry(new DiaryEntry(title, content, date));
                        } else if (currentLevel != null) {
                            System.out.println("Level not completed — entry locked.");
                        }
                    }
                }
                case 2 -> diary.viewEntries();
                case 3 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option!");
            }
        } while (ch != 3);
    }

    // ------------------- Mood Section -------------------
    private static void moodSection(Scanner sc, MoodTracker moodTracker, User currentUser, User[] allUsers, int userCount) {
        int ch;
        do {
            System.out.println("\n=== MOOD TRACKER ===");
            System.out.println("1. Add Mood");
            System.out.println("2. View Moods");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Enter a valid number!");
                sc.next();
            }
            ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> {
                    System.out.print("Enter your mood: ");
                    String mood = sc.nextLine();
                    moodTracker.addMood(mood);
                }
                case 2 -> {
                    if (currentUser instanceof AdminUser) {
                        System.out.print("Enter username to view moods: ");
                        String targetUser = sc.nextLine();
                        MoodTracker mt = new MoodTracker(targetUser);
                        mt.viewMoods();
                    } else moodTracker.viewMoods();
                }
                case 3 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option!");
            }
        } while (ch != 3);
    }

    // ------------------- Reminder Section -------------------
    private static void reminderSection(Scanner sc, ReminderManager rm, User currentUser, User[] allUsers, int userCount) {
        int ch;
        do {
            System.out.println("\n=== REMINDER MENU ===");
            System.out.println("1. Add Reminder");
            System.out.println("2. View Reminders");
            System.out.println("3. Delete Reminder");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Enter a valid number!");
                sc.next();
            }
            ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> {
                    System.out.print("Enter date/time for reminder (e.g., 17 Oct 2025): ");
                    String date = sc.nextLine();
                    System.out.print("Enter reminder message: ");
                    String message = sc.nextLine();
                    rm.addReminder(date, message);
                }
                case 2 -> {
                    if (currentUser instanceof AdminUser) {
                        System.out.print("Enter username to view reminders: ");
                        String targetUser = sc.nextLine();
                        ReminderManager rManager = new ReminderManager(targetUser);
                        rManager.viewReminders();
                    } else rm.viewReminders();
                }
                case 3 -> {
                    if (currentUser instanceof AdminUser) {
                        System.out.print("Enter username to delete reminders: ");
                        String targetUser = sc.nextLine();
                        ReminderManager rManager = new ReminderManager(targetUser);
                        rManager.viewReminders();
                        System.out.print("Enter reminder number to delete: ");
                        int index = sc.nextInt();
                        sc.nextLine();
                        rManager.disableReminder(index);
                    } else {
                        rm.viewReminders();
                        System.out.print("Enter reminder number to delete: ");
                        int index = sc.nextInt();
                        sc.nextLine();
                        rm.disableReminder(index);
                    }
                }
                case 4 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option!");
            }
        } while (ch != 4);
    }

    // ------------------- To-Do Section -------------------
    private static void todoSection(Scanner sc, ToDoListManager tm, User currentUser, User[] allUsers, int userCount) {
        int ch;
        do {
            System.out.println("\n=== TO-DO LIST MENU ===");
            System.out.println("1. Add New Task");
            System.out.println("2. View All Tasks");
            System.out.println("3. Mark Task as Complete");
            System.out.println("4. Remove Task");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Enter a valid number!");
                sc.next();
            }
            ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> {
                    System.out.print("Enter new task: ");
                    String task = sc.nextLine();
                    tm.addTask(task);
                }
                case 2 -> {
                    if (currentUser instanceof AdminUser) {
                        System.out.print("Enter username to view tasks: ");
                        String targetUser = sc.nextLine();
                        ToDoListManager manager = new ToDoListManager(targetUser);
                        manager.viewTasks();
                    } else tm.viewTasks();
                }
                case 3 -> {
                    if (currentUser instanceof AdminUser) {
                        System.out.print("Enter username to mark task complete: ");
                        String targetUser = sc.nextLine();
                        ToDoListManager manager = new ToDoListManager(targetUser);
                        manager.viewTasks();
                        System.out.print("Enter task number: ");
                        int index = sc.nextInt();
                        sc.nextLine();
                        manager.markComplete(index);
                    } else {
                        tm.viewTasks();
                        System.out.print("Enter task number: ");
                        int index = sc.nextInt();
                        sc.nextLine();
                        tm.markComplete(index);
                    }
                }
                case 4 -> {
                    if (currentUser instanceof AdminUser) {
                        System.out.print("Enter username to remove task: ");
                        String targetUser = sc.nextLine();
                        ToDoListManager manager = new ToDoListManager(targetUser);
                        manager.viewTasks();
                        System.out.print("Enter task number: ");
                        int index = sc.nextInt();
                        sc.nextLine();
                        manager.removeTask(index);
                    } else {
                        tm.viewTasks();
                        System.out.print("Enter task number: ");
                        int index = sc.nextInt();
                        sc.nextLine();
                        tm.removeTask(index);
                    }
                }
                case 5 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option!");
            }
        } while (ch != 5);
    }

    // ------------------- Quote Section -------------------
    private static void quoteSection(Scanner sc, QuoteGenerator qg, User currentUser) {
        int ch;
        do {
            System.out.println("\n=== MOTIVATIONAL QUOTES ===");
            System.out.println("1. Show Random Quote");
            System.out.println("2. Show All Quotes");
            if (currentUser instanceof AdminUser) System.out.println("3. Add Quote");
            System.out.println(currentUser instanceof AdminUser ? "4. Back" : "3. Back");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Enter a valid number!");
                sc.next();
            }
            ch = sc.nextInt();
            sc.nextLine();

            if (currentUser instanceof AdminUser) {
                switch (ch) {
                    case 1 -> qg.showRandomQuote();
                    case 2 -> qg.showAllQuotes();
                    case 3 -> {
                        System.out.print("Enter new quote: ");
                        String newQuote = sc.nextLine();
                        qg.addQuote(newQuote);
                    }
                    case 4 -> System.out.println("Returning to main menu...");
                    default -> System.out.println("Invalid option!");
                }
            } else {
                switch (ch) {
                    case 1 -> qg.showRandomQuote();
                    case 2 -> qg.showAllQuotes();
                    case 3 -> System.out.println("Returning to main menu...");
                    default -> System.out.println("Invalid option!");
                }
            }
        } while ((currentUser instanceof AdminUser && ch != 4) || (!(currentUser instanceof AdminUser) && ch != 3));
    }

    // ------------------- Fun Zone Section -------------------
    private static void funZoneSection(Scanner sc, FunZoneManager funZone, User currentUser) {
        int ch;
        do {
            funZone.displayMenu();
            TriviaGame triviaGame = new TriviaGame();

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Enter a number!");
                sc.next();
            }
            ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                case 1 -> {
                    System.out.println("\nWelcome to Guess the Number!");
                    System.out.print("Enter your guess (1–10): ");

                    while (!sc.hasNextInt()) {
                        System.out.println("Invalid input. Enter a number between 1–10!");
                        sc.next();
                    }
                    int guess = sc.nextInt();
                    sc.nextLine();

                    funZone.runGameByChoice(1, new int[]{guess}, null);
                }
                case 2-> {
                    // Step 1: Check if current user is admin
                    String[] adminFlag = currentUser instanceof AdminUser ? new String[]{"admin"} : null;

                    // Step 2: Display trivia question or admin menu
                    triviaGame.playGame("", adminFlag);

                    // Step 3: Ask user for their answer
                    System.out.print("Enter your answer: ");
                    String userAnswer = sc.nextLine();  // Make sure 'scanner' is defined elsewhere

                    // Step 4: Submit user's answer
                    triviaGame.playGame(userAnswer, adminFlag);

                    break;
                }
                case 3 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice!");
            }

        } while (ch != 3);
    }

    // ------------------- Admin Dashboard -------------------
    private static void adminDashboard(Scanner sc, User[] allUsers, int userCount, QuoteGenerator qg) {
        int ch;
        do {
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("1. List All Users");
            System.out.println("2. Manage Quotes");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Enter a valid number!");
                sc.next();
            }
            ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> {
                    System.out.println("\n=== USERS ===");
                    for (int i = 0; i < userCount; i++) {
                        System.out.println((i + 1) + ". " + allUsers[i].getUsername() +
                                (allUsers[i] instanceof AdminUser ? " [Admin]" : ""));
                    }
                }
                case 2 -> {
                    System.out.print("Enter new quote to add: ");
                    String newQuote = sc.nextLine();
                    qg.addQuote(newQuote);
                }
                case 3 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option!");
            }
        } while (ch != 3);
    }

    // ------------------- MAIN METHOD -------------------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        User[] users = new User[6]; // max 6 users
        int userCount = 0;

        // Default admins
        users[userCount++] = new AdminUser("Aleeza", "1234");
        users[userCount++] = new AdminUser("Zainab", "5678");

        System.out.println("=== WELCOME TO SMART DIARY APP ===");

        int option = 0;
        while (option != 3) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Error.Enter a valid number: ");
                sc.next();
            }
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.print("Enter username: ");
                    String uname = sc.nextLine();
                    System.out.print("Enter password: ");
                    String pass = sc.nextLine();

                    if (NormalUser.registerToFile(uname, pass)) {
                        System.out.println("User registered successfully!");
                    } else {
                        System.out.println("Username already exists!");
                    }
                }
                case 2 -> {
                    System.out.print("Username: ");
                    String u = sc.nextLine();
                    System.out.print("Password: ");
                    String p = sc.nextLine();
                    boolean loggedIn = false;

                    // Admin Users memory-based
                    for (int i = 0; i < userCount; i++) {
                        if (users[i] instanceof AdminUser && users[i].login(u, p)) {
                            loggedIn = true;
                            AdminUser admin = (AdminUser) users[i];
                            System.out.println(admin.adminMessage());
                            runDiaryApp(admin, users, userCount);
                            break;
                        }
                    }

                    // Normal Users file-based
                    if (!loggedIn) {
                        NormalUser normal = NormalUser.loginFromFile(u, p);
                        if (normal != null) {
                            loggedIn = true;
                            System.out.println(normal.userMessage());
                            runDiaryApp(normal, null, 0);
                        }
                    }

                    if (!loggedIn) System.out.println("Login failed!");
                }
                case 3 -> System.out.println("Exiting... Goodbye!");
                default -> System.out.println("Invalid option!");
            }
        }
    }
}
