import java.util.concurrent.CountDownLatch;

public class DeadlockDemo {
    static class Resource {
        private final String name;
        private final int id;

        Resource(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }

    public static void demonstrateResolvedLockOrdering() throws InterruptedException {
        System.out.println("\n--- Consistent Lock Ordering (Deadlock-Free Solution) ---");
        Resource resA = new Resource("Account-A", 1);
        Resource resB = new Resource("Account-B", 2);

        // Function that locks according to consistent ID order regardless of call parameters
        Runnable transferSafe1 = () -> safeTransfer(resA, resB);
        Runnable transferSafe2 = () -> safeTransfer(resB, resA);

        Thread t1 = new Thread(transferSafe1, "Thread-Transfer-A-to-B");
        Thread t2 = new Thread(transferSafe2, "Thread-Transfer-B-to-A");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println("Both transfers completed successfully without deadlock!");
    }

    private static void safeTransfer(Resource from, Resource to) {
        Resource firstLock = from.getId() < to.getId() ? from : to;
        Resource secondLock = from.getId() < to.getId() ? to : from;

        synchronized (firstLock) {
            System.out.println(Thread.currentThread().getName() + " acquired first lock on " + firstLock.getName());
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}

            synchronized (secondLock) {
                System.out.println(Thread.currentThread().getName() + " acquired second lock on " + secondLock.getName());
                System.out.println(Thread.currentThread().getName() + " successfully transferred between " + from.getName() + " and " + to.getName());
            }
        }
    }

    public static void demonstrateDeadlockBehavior() {
        System.out.println("\n--- Deadlock Demonstration (Opposite Lock Acquisition) ---");
        Resource resA = new Resource("Account-A", 1);
        Resource resB = new Resource("Account-B", 2);
        CountDownLatch latch = new CountDownLatch(1);

        Thread t1 = new Thread(() -> {
            synchronized (resA) {
                System.out.println("Thread 1 locked " + resA.getName() + ", waiting for " + resB.getName());
                latch.countDown();
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                synchronized (resB) {
                    System.out.println("Thread 1 locked " + resB.getName());
                }
            }
        }, "Deadlock-Thread-1");

        Thread t2 = new Thread(() -> {
            try { latch.await(); } catch (InterruptedException ignored) {}
            synchronized (resB) {
                System.out.println("Thread 2 locked " + resB.getName() + ", waiting for " + resA.getName());
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                synchronized (resA) {
                    System.out.println("Thread 2 locked " + resA.getName());
                }
            }
        }, "Deadlock-Thread-2");

        t1.setDaemon(true);
        t2.setDaemon(true);
        t1.start();
        t2.start();

        try {
            t1.join(500);
            t2.join(500);
            if (t1.isAlive() && t2.isAlive()) {
                System.out.println(">> DEADLOCK CONFIRMED: Thread 1 holds " + resA.getName() + " waiting for " + resB.getName() +
                        ", while Thread 2 holds " + resB.getName() + " waiting for " + resA.getName());
            }
        } catch (InterruptedException ignored) {}
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("========== Practical 10 - Problem 3: Deadlock & Lock Ordering ==========");
        demonstrateDeadlockBehavior();
        demonstrateResolvedLockOrdering();
    }
}
