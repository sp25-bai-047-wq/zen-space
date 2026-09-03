package com.example.backup;

import java.io.*;
import java.util.*;

public class QuoteGenerator {

    private static final String FILE_PREFIX = "quotes_";
    private final List<Quote> quotes;
    private final String username;

    /**
     * Constructor initializes the QuoteGenerator and loads existing quotes from file.
     * @param user The currently logged-in User.
     */
    public QuoteGenerator(User user) {
        this.username = user.getUsername();
        this.quotes = new ArrayList<>();
        loadQuotes();

        // Ensure at least one default quote exists if the file is empty
        if (quotes.isEmpty()) {
            addDefaultQuote();
        }
    }

    private String getFileName() {
        return FILE_PREFIX + username + ".txt";
    }

    private void addDefaultQuote() {
        // This is a failsafe to ensure getRandomQuote doesn't crash on an empty file.
        quotes.add(new Quote("The best way to predict the future is to create it.", "Peter Drucker"));
    }

    // --- Persistence Methods ---

    private void loadQuotes() {
        File file = new File(getFileName());
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // File format: QuoteText::Author
                String[] parts = line.split("::", 2);
                if (parts.length == 2) {
                    quotes.add(new Quote(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading quotes for user " + username + ": " + e.getMessage());
        }
    }

    private boolean saveQuotes() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFileName()))) {
            for (Quote quote : quotes) {
                // Save format: QuoteText::Author
                bw.write(quote.getQuoteText() + "::" + quote.getAuthor() + "\n");
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving quotes for user " + username + ": " + e.getMessage());
            return false;
        }
    }

    // --- Core Logic Methods ---

    public Quote getRandomQuote() {
        if (quotes.isEmpty()) {
            // Should not happen if addDefaultQuote runs, but safe check anyway
            return null;
        }
        Random rand = new Random();
        return quotes.get(rand.nextInt(quotes.size()));
    }

    public List<Quote> getAllQuotes() {
        return Collections.unmodifiableList(quotes); // Return read-only list
    }

    public String addQuote(String text, String author) {
        if (text == null || text.trim().isEmpty()) {
            return "Quote text cannot be empty.";
        }
        if (author == null || author.trim().isEmpty()) {
            author = "Unknown"; // Default author if not provided
        }

        Quote newQuote = new Quote(text.trim(), author.trim());
        quotes.add(newQuote);

        if (saveQuotes()) {
            return "Quote successfully added!";
        } else {
            // Revert changes if save fails
            quotes.remove(newQuote);
            return "Error saving quote to file.";
        }
    }

    // Optional: Method to remove a quote (useful for CRUD)
    public boolean removeQuote(Quote quote) {
        if (quotes.remove(quote)) {
            return saveQuotes();
        }
        return false;
    }
}
