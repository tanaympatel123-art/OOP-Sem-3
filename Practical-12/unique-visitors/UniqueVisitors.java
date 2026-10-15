import java.util.*;

public class UniqueVisitors {
    public static void main(String[] args) {
        System.out.println("========== Practical 12 - Problem 2: Unique Visitors with Sets ==========");

        List<String> rawLogins = List.of(
                "tanay_p", "alice99", "bob_dev", "tanay_p",
                "carol_k", "alice99", "david_w", "eve_sec", "bob_dev"
        );

        System.out.println("All Login Attempts (" + rawLogins.size() + " total): " + rawLogins);

        // HashSet for counting unique visitors
        Set<String> hashSet = new HashSet<>(rawLogins);
        System.out.println("Total Unique Users (via HashSet): " + hashSet.size());

        // LinkedHashSet to preserve first-seen insertion order
        Set<String> linkedHashSet = new LinkedHashSet<>(rawLogins);
        System.out.println("Unique Users in First-Seen Order: " + linkedHashSet);

        // Print first 3 distinct names
        System.out.println("\nFirst Three Distinct Visitors:");
        int count = 0;
        for (String user : linkedHashSet) {
            System.out.println("  " + (count + 1) + ". " + user);
            count++;
            if (count == 3) break;
        }
    }
}
