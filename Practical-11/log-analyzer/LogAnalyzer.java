import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LogAnalyzer {
    public static void main(String[] args) {
        System.out.println("========== Practical 11 - Problem 2: NIO Log Analyzer ==========");

        Path tempDir = Path.of("temp_logs_dir");
        Path log1 = tempDir.resolve("server.log");
        Path log2 = tempDir.resolve("database.log");

        try {
            Files.createDirectories(tempDir);

            Files.write(log1, List.of(
                    "2026-10-15 10:00:01 INFO Server started on port 8080",
                    "2026-10-15 10:00:05 WARN High memory usage detected: 78%",
                    "2026-10-15 10:01:20 ERROR Connection refused from 192.168.1.10"
            ));

            Files.write(log2, List.of(
                    "2026-10-15 10:00:02 INFO Database connected: charusat_db",
                    "2026-10-15 10:02:15 ERROR Deadlock detected on transaction #409",
                    "2026-10-15 10:02:16 INFO Transaction rolled back successfully"
            ));

            System.out.println("Created sample log files in " + tempDir.toAbsolutePath());
            System.out.println("\n--- Scanning for 'ERROR' entries across log files ---");

            try (var stream = Files.newDirectoryStream(tempDir, "*.log")) {
                for (Path entry : stream) {
                    System.out.println("Scanning file: " + entry.getFileName());
                    try (BufferedReader reader = Files.newBufferedReader(entry)) {
                        String line;
                        int lineNum = 1;
                        while ((line = reader.readLine()) != null) {
                            if (line.contains("ERROR")) {
                                System.out.println("  [Line " + lineNum + "] " + line);
                            }
                            lineNum++;
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("File I/O Error: " + e.getMessage());
        } finally {
            // Clean up
            try {
                Files.deleteIfExists(log1);
                Files.deleteIfExists(log2);
                Files.deleteIfExists(tempDir);
            } catch (IOException ignored) {}
        }
    }
}
