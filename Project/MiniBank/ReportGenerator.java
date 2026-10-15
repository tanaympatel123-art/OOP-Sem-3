import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class ReportGenerator {

    public static void generateReport(Path logsFolder, Path reportFile) throws IOException {
        long totalDeposits = 0;
        long totalWithdrawals = 0;
        int fileCount = 0;
        long totalLogSizeBytes = 0;

        StringBuilder details = new StringBuilder();

        if (Files.exists(logsFolder)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsFolder, "*.log")) {
                for (Path logFile : stream) {
                    BasicFileAttributes attrs = Files.readAttributes(logFile, BasicFileAttributes.class);
                    if (attrs.size() == 0) {
                        continue; // skip empty files
                    }

                    fileCount++;
                    totalLogSizeBytes += attrs.size();

                    details.append(String.format("File: %s | Size: %d bytes | Modified: %s\n",
                            logFile.getFileName(), attrs.size(), attrs.lastModifiedTime()));

                    try (BufferedReader reader = Files.newBufferedReader(logFile)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String trimmed = line.trim();
                            if (trimmed.isEmpty()) continue;

                            String[] parts = trimmed.split("\\s+");
                            if (parts.length >= 3) {
                                String action = parts[0].toUpperCase();
                                try {
                                    long amount = Long.parseLong(parts[2]);
                                    if ("DEPOSIT".equals(action)) {
                                        totalDeposits += amount;
                                    } else if ("WITHDRAW".equals(action)) {
                                        totalWithdrawals += amount;
                                    }
                                } catch (NumberFormatException ignored) {}
                            }
                        }
                    }
                }
            }
        }

        long netChange = totalDeposits - totalWithdrawals;

        if (reportFile.getParent() != null) {
            Files.createDirectories(reportFile.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(reportFile)) {
            writer.write("====================================================\n");
            writer.write("             MINIBANK TRANSACTION SUMMARY REPORT    \n");
            writer.write("====================================================\n");
            writer.write(String.format("Log Files Scanned   : %d\n", fileCount));
            writer.write(String.format("Total Log Size      : %d bytes\n", totalLogSizeBytes));
            writer.write("----------------------------------------------------\n");
            writer.write(String.format("Total Deposits      : Rs. %d\n", totalDeposits));
            writer.write(String.format("Total Withdrawals   : Rs. %d\n", totalWithdrawals));
            writer.write(String.format("Net Balance Change  : Rs. %d\n", netChange));
            writer.write("----------------------------------------------------\n");
            writer.write("Scanned Files Details:\n");
            writer.write(details.toString());
            writer.write("====================================================\n");
        }
    }

    public static void main(String[] args) {
        System.out.println("========== MiniBank Part B - Practical 11: Persistence & Reporting ==========");

        Path dataDir = Path.of("minibank_test_data");
        Path accountsFile = dataDir.resolve("accounts.dat");
        Path logsDir = dataDir.resolve("logs");
        Path reportFile = dataDir.resolve("end_of_day_report.txt");

        try {
            // 1. Save and reload accounts
            System.out.println("\n--- 1. Testing StatePersister (Save/Load) ---");
            Account[] accounts = new Account[]{
                    new SavingsAccount("Tanay Patel", 5000, 1000),
                    new CurrentAccount("Company A", 12000, 5000),
                    new FixedDepositAccount("Investor B", 25000)
            };

            System.out.println("Saving accounts to " + accountsFile + ":");
            for (Account a : accounts) {
                System.out.println("  " + a);
            }
            StatePersister.save(accounts, accountsFile);

            Account[] loadedAccounts = StatePersister.load(accountsFile);
            System.out.println("\nLoaded accounts from " + accountsFile + ":");
            for (Account a : loadedAccounts) {
                System.out.println("  " + a);
            }

            // 2. Transaction logs
            System.out.println("\n--- 2. Appending Transaction Logs ---");
            Path dailyLog = logsDir.resolve("transactions_day1.log");
            TransactionLog.append(dailyLog, "DEPOSIT AC0001 5000");
            TransactionLog.append(dailyLog, "WITHDRAW AC0002 2000");
            TransactionLog.append(dailyLog, "DEPOSIT AC0003 1500");
            System.out.println("Appended 3 transactions to " + dailyLog);

            // 3. Generate summary report
            System.out.println("\n--- 3. Generating Report via NIO ---");
            generateReport(logsDir, reportFile);

            System.out.println("Report generated. Contents of " + reportFile.getFileName() + ":");
            System.out.println(Files.readString(reportFile));

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clean up test data
            try {
                Files.deleteIfExists(reportFile);
                Files.deleteIfExists(logsDir.resolve("transactions_day1.log"));
                Files.deleteIfExists(logsDir);
                Files.deleteIfExists(accountsFile);
                Files.deleteIfExists(dataDir);
            } catch (IOException ignored) {}
        }
    }
}
