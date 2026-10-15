import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolRunner {
    public static void main(String[] args) {
        System.out.println("========== Practical 10 - Problem 1: Thread-Pool Runner ==========");
        final int POOL_SIZE = 3;
        final int TOTAL_TASKS = 10;

        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);
        System.out.println("Created fixed thread pool of size " + POOL_SIZE);

        for (int i = 1; i <= TOTAL_TASKS; i++) {
            final int taskId = i;
            executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Task #" + taskId + " started on " + threadName);
                try {
                    Thread.sleep(150); // simulate work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Task #" + taskId + " completed on " + threadName);
            });
        }

        executor.shutdown();
        try {
            boolean completed = executor.awaitTermination(5, TimeUnit.SECONDS);
            System.out.println("All tasks finished within time: " + completed);
        } catch (InterruptedException e) {
            System.err.println("Thread pool interrupted.");
        }
    }
}
