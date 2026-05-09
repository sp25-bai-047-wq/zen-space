package ProjectPhase1;

import java.io.*;
import java.util.*;

public class MoodTracker {

    private final String username;
    private final boolean isAdmin;
    private final List<String> moods;

    public MoodTracker(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
        moods = new ArrayList<>();
        if (!isAdmin) loadMoods(); // Admin doesn't load a single file
    }
    public MoodTracker(String username) {
        this(username, false); // default isAdmin to false
    }


    private String getFileName(String user) {
        return "moods_" + user + ".txt";
    }

    // Load moods for a normal user
    private void loadMoods() {
        File file = new File(getFileName(username));
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) moods.add(line);
        } catch (IOException e) {
            Output.println("Error loading moods: " + e.getMessage());
        }
    }

    // Save moods for a normal user
    private void saveMoods() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFileName(username)))) {
            for (String mood : moods) bw.write(mood + "\n");
        } catch (IOException e) {
            Output.println("Error saving moods: " + e.getMessage());
        }
    }

    // User adds a mood
    public void addMood(String mood) {
        if (isAdmin) {
            Output.println("Admin cannot add moods to a personal list.");
            return;
        }
        if (mood == null || mood.trim().isEmpty()) {
            Output.println("Mood cannot be empty.");
            return;
        }
        moods.add(mood);
        saveMoods();
        Output.println("Mood added successfully!");
    }

    // User views their moods
    public void viewMoods() {
        if (isAdmin) {
            Output.println("Admin cannot view a single user's moods this way.");
            return;
        }
        Output.println("\n=== Your Recorded Moods ===");
        if (moods.isEmpty()) {
            Output.println("No moods recorded yet.");
            return;
        }
        for (int i = 0; i < moods.size(); i++) {
            Output.println((i + 1) + ". " + moods.get(i));
        }
    }

    // Admin views all users' moods
    public void viewAllMoods() {
        if (!isAdmin) {
            Output.println("You are not an admin!");
            return;
        }

        File folder = new File(".");
        File[] files = folder.listFiles((dir, name) -> name.startsWith("moods_") && name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            Output.println("No mood records found.");
            return;
        }

        for (File file : files) {
            String user = file.getName().replace("moods_", "").replace(".txt", "");
            Output.println("\n--- Moods of user: " + user + " ---");
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                int count = 1;
                while ((line = br.readLine()) != null) {
                    Output.println(count + ". " + line);
                    count++;
                }
                if (count == 1) Output.println("No moods recorded.");
            } catch (IOException e) {
                Output.println("Error reading moods for " + user + ": " + e.getMessage());
            }
        }
    }

    // Admin deletes a mood for any user
    public void deleteMoodForUser(String targetUser, int moodNumber) {
        if (!isAdmin) {
            Output.println("You are not an admin!");
            return;
        }

        File file = new File(getFileName(targetUser));
        if (!file.exists()) {
            Output.println("No mood file found for user: " + targetUser);
            return;
        }

        List<String> userMoods = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) userMoods.add(line);
        } catch (IOException e) {
            Output.println("Error reading moods: " + e.getMessage());
            return;
        }

        int index = moodNumber - 1;
        if (index < 0 || index >= userMoods.size()) {
            Output.println("Invalid mood number.");
            return;
        }

        String removed = userMoods.remove(index);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String mood : userMoods) bw.write(mood + "\n");
        } catch (IOException e) {
            Output.println("Error saving moods: " + e.getMessage());
            return;
        }

        Output.println("Removed mood for " + targetUser + ": " + removed);
    }
}
