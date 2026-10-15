import java.util.*;

public class Leaderboard {
    public static void main(String[] args) {
        System.out.println("========== Practical 12 - Problem 3: Leaderboard Sorting ==========");

        Map<String, Integer> scores = new HashMap<>();
        scores.put("Tanay", 950);
        scores.put("Riya", 980);
        scores.put("Aarav", 850);
        scores.put("Pooja", 950);
        scores.put("Kabir", 920);
        scores.put("Ananya", 980);
        scores.put("Dev", 810);

        System.out.println("Raw Player Scores: " + scores);

        // Sort entries: score descending, tie broken by player name ascending
        List<Map.Entry<String, Integer>> leaderboard = new ArrayList<>(scores.entrySet());
        leaderboard.sort(
                Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey())
        );

        final int TOP_N = 5;
        System.out.println("\n--- Top " + TOP_N + " Leaderboard ---");
        System.out.printf("%-6s %-15s %-10s\n", "Rank", "Player", "Score");
        System.out.println("----------------------------------");

        for (int i = 0; i < Math.min(TOP_N, leaderboard.size()); i++) {
            Map.Entry<String, Integer> entry = leaderboard.get(i);
            System.out.printf("#%-5d %-15s %-10d\n", (i + 1), entry.getKey(), entry.getValue());
        }
    }
}
