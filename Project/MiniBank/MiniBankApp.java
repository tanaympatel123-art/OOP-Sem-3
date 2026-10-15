import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class MiniBankApp {
    private static final Path DATA_FILE = Path.of("data/accounts.dat");
    private static final Path LOGS_DIR = Path.of("data/logs");
    private static final Path REPORT_FILE = Path.of("data/reports/end_of_day_report.txt");

    private final BankService service;

    public MiniBankApp() {
        this.service = new BankService();
        loadState();
    }

    private void loadState() {
        if (Files.exists(DATA_FILE)) {
            try {
                Account[] loaded = StatePersister.load(DATA_FILE);
                for (Account acc : loaded) {
                    service.getRepository().save(acc);
                }
                System.out.println("Loaded " + loaded.length + " accounts from persistent storage.");
            } catch (Exception e) {
                System.err.println("Failed to load saved state, seeding default accounts: " + e.getMessage());
                seedDefaults();
            }
        } else {
            seedDefaults();
        }
    }

    private void seedDefaults() {
        try {
            service.openAccount("savings", "Riya Sharma", 5000, 1000);
            service.openAccount("current", "Tanay Patel", 12000, 5000);
            service.openAccount("fd", "Suresh Mehta", 25000, 0);
        } catch (BankException ignored) {}
    }

    public void saveState() {
        try {
            List<Account> accounts = service.listAccounts();
            StatePersister.save(accounts.toArray(new Account[0]), DATA_FILE);
            System.out.println("Persisted " + accounts.size() + " accounts to " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("Failed to persist accounts: " + e.getMessage());
        }
    }

    public void runMenu(Scanner scanner) {
        BankInfo info = new BankInfo("MiniBank Enterprise", "CHARUSAT Branch");
        System.out.println("====================================================");
        System.out.println("       " + info.name() + " (" + info.branch() + ")");
        System.out.println("====================================================");

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter choice (1-9): ");
            String input = scanner.hasNextLine() ? scanner.nextLine().trim() : "9";

            switch (input) {
                case "1" -> handleOpenAccount(scanner);
                case "2" -> handleDeposit(scanner);
                case "3" -> handleWithdraw(scanner);
                case "4" -> handleTransfer(scanner);
                case "5" -> handleStatement(scanner);
                case "6" -> handleListAccounts();
                case "7" -> handleConcurrentBatch();
                case "8" -> handleGenerateReport();
                case "9" -> {
                    saveState();
                    running = false;
                    System.out.println("Thank you for using MiniBank. Goodbye!");
                }
                default -> System.out.println("Invalid option. Please choose between 1 and 9.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n----------------- MINI-BANK MENU -----------------");
        System.out.println("1. Open New Account");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Transfer Money");
        System.out.println("5. View Account Statement");
        System.out.println("6. List All Accounts (Sorted by Balance)");
        System.out.println("7. Run Concurrent Batch Processor (Practical 10 & 12)");
        System.out.println("8. Generate End-of-Day Audit Report (Practical 11)");
        System.out.println("9. Save & Exit");
        System.out.println("--------------------------------------------------");
    }

    private void handleOpenAccount(Scanner scanner) {
        try {
            System.out.print("Account Type (savings / current / fd): ");
            String type = scanner.nextLine().trim();
            System.out.print("Owner Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Opening Balance: Rs. ");
            long balance = Long.parseLong(scanner.nextLine().trim());
            Account acc = service.openAccount(type, name, balance, 1000);
            System.out.println("Successfully opened account: " + acc.getAccountNumber() + " for " + acc.getOwnerName());
        } catch (Exception e) {
            System.out.println("Open Account Error: " + e.getMessage());
        }
    }

    private void handleDeposit(Scanner scanner) {
        try {
            System.out.print("Account Number: ");
            String accNum = scanner.nextLine().trim();
            System.out.print("Amount to Deposit: Rs. ");
            long amt = Long.parseLong(scanner.nextLine().trim());
            service.deposit(accNum, amt);
            System.out.println("Deposit of Rs. " + amt + " successful on " + accNum);
        } catch (Exception e) {
            System.out.println("Deposit Error: " + e.getMessage());
        }
    }

    private void handleWithdraw(Scanner scanner) {
        try {
            System.out.print("Account Number: ");
            String accNum = scanner.nextLine().trim();
            System.out.print("Amount to Withdraw: Rs. ");
            long amt = Long.parseLong(scanner.nextLine().trim());
            service.withdraw(accNum, amt);
            System.out.println("Withdrawal of Rs. " + amt + " successful on " + accNum);
        } catch (Exception e) {
            System.out.println("Withdrawal Error: " + e.getMessage());
        }
    }

    private void handleTransfer(Scanner scanner) {
        try {
            System.out.print("From Account Number: ");
            String from = scanner.nextLine().trim();
            System.out.print("To Account Number: ");
            String to = scanner.nextLine().trim();
            System.out.print("Amount to Transfer: Rs. ");
            long amt = Long.parseLong(scanner.nextLine().trim());
            service.transfer(from, to, amt);
            System.out.println("Transfer of Rs. " + amt + " from " + from + " to " + to + " completed safely.");
        } catch (Exception e) {
            System.out.println("Transfer Error: " + e.getMessage());
        }
    }

    private void handleStatement(Scanner scanner) {
        try {
            System.out.print("Account Number: ");
            String accNum = scanner.nextLine().trim();
            System.out.println(service.statement(accNum));
        } catch (Exception e) {
            System.out.println("Statement Error: " + e.getMessage());
        }
    }

    private void handleListAccounts() {
        System.out.println("\n--- All Accounts (Sorted by Balance) ---");
        List<Account> accounts = service.listAccountsSortedByBalance();
        for (Account a : accounts) {
            System.out.printf("  %-8s | %-18s | Rs. %,d\n", a.getAccountNumber(), a.getOwnerName(), a.getBalance());
        }
    }

    private void handleConcurrentBatch() {
        System.out.println("\nRunning batch transactions across threads...");
        TransactionProcessor processor = new TransactionProcessor(4);
        List<Account> accounts = service.listAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        for (int i = 0; i < 10; i++) {
            final int idx = i % accounts.size();
            final Account acc = accounts.get(idx);
            processor.submit(() -> {
                try {
                    acc.deposit(100);
                } catch (InvalidAmountException ignored) {}
            });
        }
        processor.stop();
        System.out.println("Concurrent batch transactions finished successfully.");
    }

    private void handleGenerateReport() {
        try {
            ReportGenerator.generateReport(LOGS_DIR, REPORT_FILE);
            if (Files.exists(REPORT_FILE)) {
                System.out.println(Files.readString(REPORT_FILE));
            } else {
                System.out.println("No logs available yet to generate report.");
            }
        } catch (Exception e) {
            System.out.println("Report Generation Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        MiniBankApp app = new MiniBankApp();
        Scanner scanner = new Scanner(System.in);
        app.runMenu(scanner);
    }
}
