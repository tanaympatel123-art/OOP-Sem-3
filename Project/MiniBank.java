import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MiniBank {
    private static final Map<String, Account> ACCOUNTS = new HashMap<>();

    public static void main(String[] args) {
        seedAccounts();
        BankInfo bank = new BankInfo("MiniBank", "CHARUSAT Branch");
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println(bank);
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter choice: ");

            String input = scanner.nextLine().trim();
            String message = switch (input) {
                case "1" -> openAccount(scanner);
                case "2" -> deposit(scanner);
                case "3" -> withdraw(scanner);
                case "4" -> transfer(scanner);
                case "5" -> {
                    running = false;
                    yield "Thank you for using MiniBank.";
                }
                default -> "Invalid menu choice. Please try again.";
            };

            System.out.println(message);
        }
    }

    private static void seedAccounts() {
        Account savings = new SavingsAccount("Riya", 5000, 1000);
        Account current = new CurrentAccount("Tanay", 2000, 1000);
        ACCOUNTS.put(savings.getAccountNumber(), savings);
        ACCOUNTS.put(current.getAccountNumber(), current);
    }

    private static void printMenu() {
        System.out.println("\n========== MENU ==========");
        System.out.println("1. Open Savings Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Exit");
    }

    private static String openAccount(Scanner scanner) {
        System.out.print("Owner name: ");
        String ownerName = scanner.nextLine().trim();
        Account account = new SavingsAccount(ownerName, 0, 0);
        ACCOUNTS.put(account.getAccountNumber(), account);
        return "Created account: " + account.getAccountNumber();
    }

    private static String deposit(Scanner scanner) {
        try {
            Account account = findAccount(scanner);
            long amount = readAmount(scanner);
            account.deposit(amount);
            return "Deposit successful. Balance: Rs. " + account.getBalance();
        } catch (BankException | NumberFormatException exception) {
            return "Deposit failed: " + exception.getMessage();
        }
    }

    private static String withdraw(Scanner scanner) {
        try {
            Account account = findAccount(scanner);
            long amount = readAmount(scanner);
            account.withdraw(amount);
            return "Withdrawal successful. Balance: Rs. " + account.getBalance();
        } catch (BankException | NumberFormatException exception) {
            return "Withdrawal failed: " + exception.getMessage();
        }
    }

    private static String transfer(Scanner scanner) {
        try {
            System.out.print("From account number: ");
            Account sender = findAccount(scanner.nextLine().trim());
            System.out.print("To account number: ");
            Account receiver = findAccount(scanner.nextLine().trim());
            long amount = readAmount(scanner);
            sender.transfer(receiver, amount);
            return "Transfer successful.";
        } catch (BankException | NumberFormatException exception) {
            return "Transfer failed: " + exception.getMessage();
        }
    }

    private static Account findAccount(Scanner scanner)
            throws AccountNotFoundException {
        System.out.print("Account number: ");
        return findAccount(scanner.nextLine().trim());
    }

    private static Account findAccount(String accountNumber)
            throws AccountNotFoundException {
        Account account = ACCOUNTS.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    private static long readAmount(Scanner scanner) {
        System.out.print("Amount: ");
        return Long.parseLong(scanner.nextLine().trim());
    }

    static void runPracticalEightDemo() {
        Account account = new SavingsAccount("Demo", -100, 0);
        System.out.println(Arrays.toString(AnnotationValidator.validate(account)));
        System.out.println(Validator.isValidMobile("9876543210"));
        System.out.println(CommandParser.parse("DEPOSIT AC0001 500"));
    }
}
