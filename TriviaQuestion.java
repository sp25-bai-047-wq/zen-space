package com.example.backup;

import java.util.List;

public class TriviaQuestion {
    private final String questionText;
    private final List<String> options;
    private final List<Integer> correctIndices; // 0-based index of correct options

    public TriviaQuestion(String text, List<String> options, List<Integer> correctIndices) {
        this.questionText = text;
        this.options = options;
        this.correctIndices = correctIndices;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Integer> getCorrectIndices() {
        return correctIndices;
    }
}