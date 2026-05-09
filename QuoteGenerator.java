package ProjectPhase1;

import java.io.*;
import java.util.*;

public class QuoteGenerator {

    private List<String> quotes;
    private final Random random;
    private static final String FILE_NAME = "quotes.txt";
    private final boolean isAdmin; // flag to differentiate admin and normal user

    public QuoteGenerator(boolean isAdmin) {
        this.isAdmin = isAdmin;
        random = new Random();
        quotes = new ArrayList<>();
        loadQuotes();
    }

    // ================= LOAD QUOTES =================
    private void loadQuotes() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            // If file doesn't exist, create an empty one
            try {
                file.createNewFile();
            } catch (IOException e) {
                Output.println("Error creating quotes file: " + e.getMessage());
            }
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // Read all lines, ignore empty ones
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    quotes.add(line.trim());
                }
            }
        } catch (IOException e) {
            Output.println("Error loading quotes: " + e.getMessage());
        }
    }

    // ================= SAVE QUOTES =================
    private void saveQuotes() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String quote : quotes) {
                bw.write(quote);
                bw.newLine();
            }
        } catch (IOException e) {
            Output.println("Error saving quotes: " + e.getMessage());
        }
    }

    // ================= ADD QUOTE (ADMIN ONLY) =================
    public void addQuote(String quote) {
        if (!isAdmin) {
            Output.println("Access Denied: Only Admin can add quotes.");
            return;
        }

        if (quote == null || quote.trim().isEmpty()) {
            Output.println("Quote cannot be empty.");
            return;
        }

        quotes.add(quote.trim());
        saveQuotes();
        Output.println("Quote added successfully!");
    }

    // ================= DELETE QUOTE (ADMIN ONLY) =================
    public void deleteQuote(int index) {
        if (!isAdmin) {
            Output.println("Access Denied: Only Admin can delete quotes.");
            return;
        }

        int realIndex = index - 1; // user sees 1-based indexing
        if (realIndex >= 0 && realIndex < quotes.size()) {
            String removedQuote = quotes.remove(realIndex);
            saveQuotes();
            Output.println("Quote deleted: \"" + removedQuote + "\"");
        } else {
            Output.println("Invalid quote number.");
        }
    }

    // ================= SHOW RANDOM QUOTE =================
    public void showRandomQuote() {
        if (quotes.isEmpty()) {
            Output.println("No quotes available!");
            return;
        }
        int idx = random.nextInt(quotes.size());
        Output.println("\nQuote of the Day:");
        Output.println("\"" + quotes.get(idx) + "\"");
    }

    // ================= SHOW ALL QUOTES =================
    public void showAllQuotes() {
        Output.println("\n=== All Available Quotes (" + quotes.size() + ") ===");
        if (quotes.isEmpty()) {
            Output.println("No quotes available.");
            return;
        }
        for (int i = 0; i < quotes.size(); i++) {
            Output.println((i + 1) + ". " + quotes.get(i));
        }
    }
}
