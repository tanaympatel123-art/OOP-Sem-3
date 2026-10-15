import java.util.*;

public class WordFrequency {
    public static void main(String[] args) {
        System.out.println("========== Practical 12 - Problem 1: Word Frequency Counter ==========");

        String text = "Java is a popular programming language. Java is object oriented and Java is versatile. Object oriented programming in Java is powerful.";

        // Normalize text: lowercase and strip punctuation
        String[] words = text.toLowerCase().replaceAll("[^a-z0-9\\s]", "").split("\\s+");

        Map<String, Integer> freqMap = new HashMap<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
            }
        }

        // Sort by frequency descending using Comparator, then alphabetically
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(freqMap.entrySet());
        entries.sort(Map.Entry.<String, Integer>comparingByValue().reversed()
                .thenComparing(Map.Entry.comparingByKey()));

        System.out.println("Sample Text:\n\"" + text + "\"\n");
        System.out.println("Word Frequencies (Highest to Lowest):");
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.printf("  %-15s : %d\n", entry.getKey(), entry.getValue());
        }
    }
}
