public class CounterRace {
    private static int unsafeCount = 0;
    private static int safeCount = 0;
    private static final Object lock = new Object();

    private static void incrementUnsafe() {
        unsafeCount++;
    }

    private static void incrementSafe() {
        synchronized (lock) {
            safeCount++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int NUM_THREADS = 5;
        final int INCREMENTS_PER_THREAD = 10000;
        final int EXPECTED_TOTAL = NUM_THREADS * INCREMENTS_PER_THREAD;

        System.out.println("========== Practical 9 - Problem 1: Counter Race ==========");

        // 1. Without synchronization (demonstrating lost updates / race condition)
        Thread[] unsafeThreads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            unsafeThreads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    incrementUnsafe();
                }
            }, "UnsafeThread-" + (i + 1));
            unsafeThreads[i].start();
        }

        for (Thread t : unsafeThreads) {
            t.join();
        }

        System.out.println("Expected count: " + EXPECTED_TOTAL);
        System.out.println("Unsynchronized count: " + unsafeCount + " (Lost updates: " + (EXPECTED_TOTAL - unsafeCount) + ")");

        // 2. With synchronization
        Thread[] safeThreads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            safeThreads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    incrementSafe();
                }
            }, "SafeThread-" + (i + 1));
            safeThreads[i].start();
        }

        for (Thread t : safeThreads) {
            t.join();
        }

        System.out.println("Synchronized count:   " + safeCount + " (Accurate: " + (safeCount == EXPECTED_TOTAL) + ")");
    }
}
