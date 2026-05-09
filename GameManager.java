package ProjectPhase1;

import java.util.Arrays;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class GameManager {

    // --- ALL LEVEL DATA ---
    protected static final String[][] ALL_LEVEL_DATA = {
            {"SERU", "sure", "sue", "use", "rue", "user"},
            {"SLTA", "salt", "last", "sat", "at"},
            {"ITDE", "tide", "tie", "diet", "edit", "tied", "die"},
            {"ASLNO", "also", "son", "loan", "loans", "salon"},
            {"EMRST", "terms", "term", "stem", "rest", "set", "met"},
            {"KLCAB", "black", "lack", "back", "lab", "cab"},
            {"ELAD", "deal", "lead", "led", "lad", "ale"},
            {"SOWNK", "know", "knows", "own", "son", "now", "snow", "won", "owns"},
            {"ERLA", "real", "earl", "ear", "are", "ale"},
            {"DSKAE", "ask", "asked", "ads", "desk", "sake", "sad", "sea"},
            {"VERNE", "never", "ever", "nerve", "even", "eve"},
            {"ETAB", "beat", "eat", "bat", "tab", "beta", "bet", "ate"},
            {"NOTO", "onto", "ton", "not", "too", "to"},
            {"ZIESD", "side", "die", "dies", "sized", "size"},
            {"MYETP", "empty", "yet", "pet", "met", "type", "temp"},
            {"GEMA", "mega", "game", "age", "meg", "gem"},
            {"EMTI", "item", "met", "tie", "time", "emit"},
            {"HERET", "thee", "three", "tree", "the", "tee", "her", "here", "there"},
            {"ELVOS", "love", "loves", "lose", "solve", "sole"},
            {"RNTEE", "enter", "net", "ten", "tree", "teen", "rent", "tee"},
            {"TEWS", "stew", "set", "west", "wet", "sew"},
            {"ETADD", "date", "dated", "eat", "ate", "tea", "add", "dad"},
            {"EEHST", "sheet", "tee", "the", "set", "these", "she", "see", "thee"},
            {"EIDDR", "red", "die", "dried", "ride", "rid", "did", "died"},
            {"PTSO", "top", "stop", "spot", "tops", "post", "pot", "opt"},
            {"SIHTR", "shirt", "his", "hit", "sit", "sir", "stir", "this", "its", "hits"},
            {"WNOT", "own", "tow", "won", "not", "town", "ton", "now", "two"},
            {"HKASE", "shake", "ash", "ask", "sake", "sea", "she", "has"},
            {"OUTHC", "thou", "touch", "hut", "hot", "out", "cut"},
            {"DENDE", "end", "ended", "den", "need", "deed"}
    };

    // --- LEVEL CLASS ---
    public static class Level {
        private final String availableLetters;
        private final String[] targetWords;
        private final boolean[] foundStatus;

        public Level(String letters, String[] words) {
            this.availableLetters = letters.toUpperCase();
            this.targetWords = words;
            this.foundStatus = new boolean[words.length];
        }

        public int checkWord(String word) {
            if (word == null || word.isEmpty()) return 2;
            String upperWord = word.toUpperCase();

            if (!isValidComposition(upperWord))
                return 3;

            for (int i = 0; i < targetWords.length; i++) {
                if (targetWords[i].toUpperCase().equals(upperWord)) {
                    if (foundStatus[i]) return 1;
                    foundStatus[i] = true;
                    return 0;
                }
            }
            return 2;
        }

        private boolean isValidComposition(String word) {
            int[] available = getLetterCounts(availableLetters);
            int[] used = getLetterCounts(word);
            for (int i = 0; i < 26; i++) {
                if (used[i] > available[i]) return false;
            }
            return true;
        }

        private int[] getLetterCounts(String word) {
            int[] counts = new int[26];
            for (char c : word.toCharArray())
                if (c >= 'A' && c <= 'Z') counts[c - 'A']++;
            return counts;
        }

        public int getFoundCount() {
            int count = 0;
            for (boolean f : foundStatus) if (f) count++;
            return count;
        }

        public int getTotalCount() {
            return targetWords.length;
        }

        public boolean isComplete() {
            return getFoundCount() == targetWords.length;
        }

        public String getAvailableLetters() {
            return availableLetters;
        }

        public void displayProgress(int levelNum) {
            Output.println("\n--- Level " + levelNum + " Progress ---");
            for (int i = 0; i < targetWords.length; i++) {
                String word = targetWords[i].toUpperCase();
                if (foundStatus[i]) Output.print("[" + word + "] ");
                else Output.print("[" + "_".repeat(word.length()) + "] ");
            }
            Output.println("\n--------------------------");

            Output.print("Available Letters: ");
            for (char c : availableLetters.toCharArray()) System.out.print("[" + c + "] ");
            Output.println("");
        }
    }

    // --- LOAD LEVEL ---
    public static Level loadLevel(int levelIndex) {
        if (levelIndex < 1 || levelIndex > ALL_LEVEL_DATA.length) return null;

        String[] data = ALL_LEVEL_DATA[levelIndex - 1];
        String letters = data[0];
        String[] words = Arrays.copyOfRange(data, 1, data.length);

        return new Level(letters, words);
    }

    // --- PROCESS INPUT ---
    public static int processInput(Level current, String input) {
        if (current == null || input == null) return -1;

        String trimmedInput = input.trim();
        if (trimmedInput.equalsIgnoreCase("BACK")) return -1;

        int result = current.checkWord(trimmedInput);

        switch (result) {
            case 0 -> {
                Output.println("✅ Correct word: " + trimmedInput);
                if (current.isComplete()) System.out.println("\n*** LEVEL COMPLETE! ***");
            }
            case 1 -> Output.println("Already found that word!");
            case 2 -> Output.println("Not a target word.");
            case 3 -> Output.println("Invalid letters used!");
        }

        return result;
    }

    // --- START LEVEL ---
    public static Level startLevel(int levelNum) {
        Level currentLevel = loadLevel(levelNum);
        if (currentLevel == null) {
            Output.printf("Could not load level %d.%n", levelNum);
            return null;
        }

        Output.printf("%n--- Starting Word Game Level %d ---%n", levelNum);
        Output.printf("Find %d words using the letters: %s%n",
                currentLevel.getTotalCount(), currentLevel.getAvailableLetters());
        Output.println("Type 'BACK' to exit.");

        return currentLevel;
    }

    // --- PROGRESS MANAGER ---
    public static class ProgressManager {
        private static final String FILE_NAME = "progress.txt";

        public static int loadProgress() {
            try {
                File file = new File(FILE_NAME);
                if (!file.exists()) return 1;

                Scanner sc = new Scanner(file);
                if (sc.hasNextInt()) return sc.nextInt();
            } catch (Exception e) {
                Output.println("Error loading progress: " + e.getMessage());
            }
            return 1;
        }

        public static void saveProgress(int level) {
            try (PrintWriter pw = new PrintWriter(FILE_NAME)) {
                pw.println(level);
            } catch (Exception e) {
                Output.println("Error saving progress: " + e.getMessage());
            }
        }
    }
}
