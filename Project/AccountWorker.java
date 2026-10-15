public class AccountWorker implements Runnable {
    private final Account account;
    private final int times;
    private final long amount;

    public AccountWorker(Account account, int times, long amount) {
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    @Override
    public void run() {
        for (int i = 0; i < times; i++) {
            try {
                account.deposit(amount);
            } catch (InvalidAmountException e) {
                System.err.println("Deposit error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("========== MiniBank Part B - Practical 9: Thread Synchronization ==========");

        final int NUM_THREADS = 10;
        final int TIMES = 1000;
        final long AMOUNT = 1;
        final long EXPECTED_BALANCE = NUM_THREADS * TIMES * AMOUNT;

        // 1. Using MiniBank's synchronized Account class
        Account account = new SavingsAccount("Concurrent Test User", 0, 0);
        Thread[] threads = new Thread[NUM_THREADS];

        System.out.println("Initial Balance: Rs. " + account.getBalance());
        System.out.println("Spawning " + NUM_THREADS + " threads performing " + TIMES + " deposits of Rs. " + AMOUNT + " each...");

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(new AccountWorker(account, TIMES, AMOUNT), "WorkerThread-" + (i + 1));
            System.out.println(threads[i].getName() + " state before start: " + threads[i].getState());
        }

        // Start all threads
        for (Thread t : threads) {
            t.start();
            System.out.println(t.getName() + " state after start: " + t.getState());
        }

        // Join all threads
        for (Thread t : threads) {
            t.join();
            System.out.println(t.getName() + " state after join: " + t.getState());
        }

        System.out.println("\nExpected Balance: Rs. " + EXPECTED_BALANCE);
        System.out.println("Final Account Balance: Rs. " + account.getBalance());
        System.out.println("Thread Safety Status: " + (account.getBalance() == EXPECTED_BALANCE ? "PASS (Synchronized)" : "FAIL (Race Condition)"));
    }
}
