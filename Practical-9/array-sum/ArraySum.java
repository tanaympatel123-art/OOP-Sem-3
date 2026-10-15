import java.util.concurrent.atomic.AtomicLong;

public class ArraySum {
    private static long unsafeTotal = 0;
    private static long synchronizedTotal = 0;
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("========== Practical 9 - Problem 3: Parallel Array Sum ==========");

        final int SIZE = 1_000_000;
        int[] numbers = new int[SIZE];
        long expectedTotal = 0;
        for (int i = 0; i < SIZE; i++) {
            numbers[i] = 1; // sum should be 1,000,000
            expectedTotal += numbers[i];
        }

        final int NUM_THREADS = 4;
        final int CHUNK_SIZE = SIZE / NUM_THREADS;

        // 1. Unsynchronized shared total (Race Condition)
        unsafeTotal = 0;
        Thread[] threads1 = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            final int start = i * CHUNK_SIZE;
            final int end = (i == NUM_THREADS - 1) ? SIZE : start + CHUNK_SIZE;
            threads1[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    unsafeTotal += numbers[j];
                }
            });
            threads1[i].start();
        }
        for (Thread t : threads1) t.join();
        System.out.println("Expected Sum:       " + expectedTotal);
        System.out.println("1. Unsafe Shared:   " + unsafeTotal + " (Incorrect due to lost updates)");

        // 2. Synchronized shared total
        synchronizedTotal = 0;
        long tStartSync = System.nanoTime();
        Thread[] threads2 = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            final int start = i * CHUNK_SIZE;
            final int end = (i == NUM_THREADS - 1) ? SIZE : start + CHUNK_SIZE;
            threads2[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    synchronized (lock) {
                        synchronizedTotal += numbers[j];
                    }
                }
            });
            threads2[i].start();
        }
        for (Thread t : threads2) t.join();
        long tEndSync = System.nanoTime();
        System.out.println("2. Synchronized:    " + synchronizedTotal +
                " | Time: " + (tEndSync - tStartSync) / 1_000_000 + " ms");

        // 3. Partial Sums per thread (Map-Reduce pattern: local accumulation)
        long tStartPartial = System.nanoTime();
        long[] partialSums = new long[NUM_THREADS];
        Thread[] threads3 = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadIndex = i;
            final int start = i * CHUNK_SIZE;
            final int end = (i == NUM_THREADS - 1) ? SIZE : start + CHUNK_SIZE;
            threads3[i] = new Thread(() -> {
                long localSum = 0;
                for (int j = start; j < end; j++) {
                    localSum += numbers[j];
                }
                partialSums[threadIndex] = localSum;
            });
            threads3[i].start();
        }
        for (Thread t : threads3) t.join();
        long finalReducedTotal = 0;
        for (long part : partialSums) {
            finalReducedTotal += part;
        }
        long tEndPartial = System.nanoTime();
        System.out.println("3. Partial Sums:    " + finalReducedTotal +
                " | Time: " + (tEndPartial - tStartPartial) / 1_000_000 + " ms (Fastest, zero lock contention)");
    }
}
