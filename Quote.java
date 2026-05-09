package com.example.backup;

public class Quote {
    private String quoteText;
    private String author;

    public Quote(String quoteText, String author) {
        this.quoteText = quoteText;
        this.author = author;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return quoteText + " - " + author;
    }
}