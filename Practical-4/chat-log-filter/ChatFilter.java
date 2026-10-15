import java.util.Scanner;

public class ChatFilter {
    public static void main(String[] args) {
        String[] logs = {"10:05 alice Hello there", "10:10 bob See you", "broken line", "10:15 riya HELLO again"};
        Scanner scanner = new Scanner(System.in);
        System.out.print("Keyword: ");
        String keyword = scanner.nextLine().toLowerCase();
        int matches = 0;
        StringBuilder report = new StringBuilder();
        for (String log : logs) {
            String[] parts = log.split(" ", 3);
            if (parts.length == 3 && parts[2].toLowerCase().contains(keyword)) {
                matches++;
                report.append(parts[0]).append(' ').append(parts[1]).append(": ").append(parts[2]).append('\n');
            }
        }
        System.out.println("Matches: " + matches);
        System.out.print(report);
    }
}
