import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AccountStore {
    private final Map<String, Account> accounts = new HashMap<>();
    private final List<String> transactionHistory = new ArrayList<>();

    public void add(Account account) {
        if (account != null) {
            accounts.put(account.getAccountNumber(), account);
        }
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public List<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public void logTransaction(String entry) {
        transactionHistory.add(entry);
    }

    public List<String> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> list = new ArrayList<>(accounts.values());
        list.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return list;
    }

    public ArrayList<Account> sortedByAccountNumber() {
        ArrayList<Account> list = new ArrayList<>(accounts.values());
        list.sort(Comparator.comparing(Account::getAccountNumber));
        return list;
    }

    public ConcurrentHashMap<String, Account> toConcurrentMap() {
        return new ConcurrentHashMap<>(accounts);
    }

    public static void main(String[] args) {
        System.out.println("========== MiniBank Part B - Practical 12: Account Store & Collections ==========");

        AccountStore store = new AccountStore();

        // Seed 3 accounts with balances 5000, 2000, 9000 as specified in rubric
        Account a1 = new SavingsAccount("Alice", 5000, 1000);
        Account a2 = new CurrentAccount("Bob", 2000, 1000);
        Account a3 = new SavingsAccount("Charlie", 9000, 2000);

        store.add(a1);
        store.add(a2);
        store.add(a3);

        store.logTransaction("Opened savings account for Alice with 5000");
        store.logTransaction("Opened current account for Bob with 2000");
        store.logTransaction("Opened savings account for Charlie with 9000");

        // Lookup test
        System.out.println("\n--- 1. Account Lookup by ID (get(a2.getAccountNumber())) ---");
        Account fetched = store.get(a2.getAccountNumber());
        System.out.println("Found Account: " + fetched);

        // Sorting by Balance
        System.out.println("\n--- 2. Accounts Sorted by Balance (Highest to Lowest) ---");
        ArrayList<Account> sorted = store.sortedByBalance();
        for (Account a : sorted) {
            System.out.println("  " + a.getAccountNumber() + " | " + a.getOwnerName() + " | Rs. " + a.getBalance());
        }

        System.out.println("\n--- 3. Transaction History Recorded (" + store.getTransactionHistory().size() + " entries) ---");
        for (String log : store.getTransactionHistory()) {
            System.out.println("  * " + log);
        }
    }
}
