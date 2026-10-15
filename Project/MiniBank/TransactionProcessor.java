import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransactionProcessor {
    private final ExecutorService executor;
    private final ConcurrentHashMap<String, Account> sharedAccounts = new ConcurrentHashMap<>();

    public TransactionProcessor() {
        this(4);
    }

    public TransactionProcessor(int poolSize) {
        this.executor = Executors.newFixedThreadPool(poolSize);
    }

    public void registerAccount(Account account) {
        sharedAccounts.put(account.getAccountNumber(), account);
    }

    public ConcurrentHashMap<String, Account> getSharedAccounts() {
        return sharedAccounts;
    }

    public void submit(Runnable task) {
        executor.execute(task);
    }

    public void stop() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // Producer-Consumer Transaction Buffer using wait() and notify()
    public static class TransactionBuffer {
        private final Queue<Command> queue = new LinkedList<>();
        private final int capacity;

        public TransactionBuffer(int capacity) {
            this.capacity = capacity;
        }

        public synchronized void addTransaction(Command cmd) throws InterruptedException {
            while (queue.size() == capacity) {
                wait();
            }
            queue.add(cmd);
            notifyAll();
        }

        public synchronized Command processNext() throws InterruptedException {
            while (queue.isEmpty()) {
                wait();
            }
            Command cmd = queue.poll();
            notifyAll();
            return cmd;
        }
    }

    // Deadlock-free transfer using consistent lock ordering (smaller accountNumber first)
    public static void safeTransfer(Account from, Account to, long amount) throws BankException {
        if (from.equals(to)) {
            return;
        }

        boolean order = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0;
        Account firstLock = order ? from : to;
        Account secondLock = order ? to : from;

        synchronized (firstLock) {
            synchronized (secondLock) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("========== MiniBank Part B - Practical 10: Transaction Engine ==========");

        // 1. Thread Pool Processing
        TransactionProcessor processor = new TransactionProcessor(4);
        Account acc1 = new SavingsAccount("Alice", 10000, 1000);
        Account acc2 = new CurrentAccount("Bob", 10000, 5000);
        processor.registerAccount(acc1);
        processor.registerAccount(acc2);

        System.out.println("\n--- 1. Batch Execution on Thread Pool ---");
        for (int i = 0; i < 20; i++) {
            final int taskId = i;
            processor.submit(() -> {
                try {
                    if (taskId % 2 == 0) {
                        acc1.deposit(100);
                    } else {
                        acc1.withdraw(50);
                    }
                } catch (BankException ignored) {}
            });
        }
        processor.stop();
        System.out.println("Batch tasks completed. Alice balance: Rs. " + acc1.getBalance());

        // 2. Producer-Consumer Demo
        System.out.println("\n--- 2. Producer-Consumer Transaction Coordination ---");
        TransactionBuffer buffer = new TransactionBuffer(3);
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    Command cmd = new Command(TransactionType.DEPOSIT, "AC0001", i * 100L);
                    buffer.addTransaction(cmd);
                    System.out.println("Buffered: " + cmd);
                    Thread.sleep(50);
                }
            } catch (InterruptedException ignored) {}
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    Command cmd = buffer.processNext();
                    System.out.println("Processed from buffer: " + cmd);
                    Thread.sleep(80);
                }
            } catch (InterruptedException ignored) {}
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        // 3. Deadlock Prevention via Consistent Lock Ordering
        System.out.println("\n--- 3. Deadlock Prevention with Consistent Lock Ordering ---");
        Thread t1 = new Thread(() -> {
            try {
                for (int i = 0; i < 50; i++) {
                    safeTransfer(acc1, acc2, 10);
                }
            } catch (BankException ignored) {}
        }, "Transfer-A-to-B");

        Thread t2 = new Thread(() -> {
            try {
                for (int i = 0; i < 50; i++) {
                    safeTransfer(acc2, acc1, 10);
                }
            } catch (BankException ignored) {}
        }, "Transfer-B-to-A");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("50 bidirectional transfers completed without deadlock!");
        System.out.println("Final Alice: Rs. " + acc1.getBalance() + " | Final Bob: Rs. " + acc2.getBalance());
    }
}
