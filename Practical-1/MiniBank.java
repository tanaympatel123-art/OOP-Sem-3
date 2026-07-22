import java.util.Scanner;

public class MiniBank {

    // Record
    record BankInfo(String name, String branch) {}

    // Enum
    enum MenuOption {
        OPEN_ACCOUNT,
        DEPOSIT,
        WITHDRAW,
        TRANSFER,
        EXIT
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Bank Header
        BankInfo bank = new BankInfo("MiniBank", "CHARUSAT Branch");

        System.out.println("================================");
        System.out.println("      " + bank.name());
        System.out.println("      " + bank.branch());
        System.out.println("================================");

        while (true) {

            // Menu
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            // Convert number to enum
            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) {
                System.out.println("Invalid Choice!");
                continue;
            }

            // Switch expression
            String message = switch (option) {
                case OPEN_ACCOUNT ->
                    "Open Account - To be implemented in a later lab.";
                case DEPOSIT ->
                    "Deposit - To be implemented in a later lab.";
                case WITHDRAW ->
                    "Withdraw - To be implemented in a later lab.";
                case TRANSFER ->
                    "Transfer - To be implemented in a later lab.";
                case EXIT ->
                    "Thank you for using MiniBank!";
            };

            System.out.println(message);

            if (option == MenuOption.EXIT) {
                break;
            }
        }

        sc.close();
    }
}