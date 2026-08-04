import java.util.Scanner;

record BankInfo(String name, String branch) {}

enum MenuOption {
    OPEN_ACCOUNT,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    EXIT
}

public class MiniBank {

    public static void main(String[] args) {

        BankInfo bank = new BankInfo("MiniBank", "CHARUSAT Branch");
        System.out.println("==================================");
        System.out.println(bank);
        System.out.println("==================================");

        Scanner sc = new Scanner(System.in);

        int choice;

        do {

            System.out.println("\n========== MENU ==========");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            String message = switch (choice) {

                case 1 -> {
                    MenuOption option = MenuOption.OPEN_ACCOUNT;
                    yield option + " - To be implemented in a later lab.";
                }

                case 2 -> {
                    MenuOption option = MenuOption.DEPOSIT;
                    yield option + " - To be implemented in a later lab.";
                }

                case 3 -> {
                    MenuOption option = MenuOption.WITHDRAW;
                    yield option + " - To be implemented in a later lab.";
                }

                case 4 -> {
                    MenuOption option = MenuOption.TRANSFER;
                    yield option + " - To be implemented in a later lab.";
                }

                case 5 -> {
                    MenuOption option = MenuOption.EXIT;
                    yield option + " - Goodbye!";
                }

                default -> "Invalid Choice!";
            };

            System.out.println(message);

        } while (choice != 5);

        System.out.println("\n========== Practical 2 & 3 Demo ==========");

        Customer c1 = new Customer(
                "Tanay",
                "tanay@gmail.com",
                "9876543210",
                new Customer.Address(
                        "12 Green Street",
                        "Anand",
                        "388001"
                )
        );

        System.out.println("\nCustomer ID : " + c1.getCustomerId());

        Customer copy = c1.clone();

        System.out.println("Cloned Customer ID : " + copy.getCustomerId());

        Account[] accounts = new Account[3];

        accounts[0] = new Account("Tanay", 5000);
        accounts[1] = new Account("Rahul", 8000);
        accounts[2] = new Account("Priya");

        accounts[0].deposit(2000);
        accounts[1].withdraw(3000);
        accounts[2].deposit(1000);

        System.out.println("\nAccount Details");

        for (Account a : accounts) {
            System.out.println(a);
        }

        System.out.println();

        System.out.println("Account 1 equals Account 2 ? "
                + accounts[0].equals(accounts[1]));

        Object obj = accounts[0];

        if (obj instanceof Account) {
            System.out.println("Object is an Account.");
        }

        sc.close();
    }
}