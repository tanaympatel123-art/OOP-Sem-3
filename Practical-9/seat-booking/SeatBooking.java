public class SeatBooking {

    static class UnsafeTheater {
        int seatsLeft = 5;
        int successfulBookings = 0;

        boolean bookSeat(String passenger) {
            if (seatsLeft > 0) {
                try {
                    Thread.sleep(10); // simulate latency exposing race condition
                } catch (InterruptedException ignored) {}
                seatsLeft--;
                successfulBookings++;
                return true;
            }
            return false;
        }
    }

    static class SafeTheater {
        int seatsLeft = 5;
        int successfulBookings = 0;

        synchronized boolean bookSeat(String passenger) {
            if (seatsLeft > 0) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {}
                seatsLeft--;
                successfulBookings++;
                return true;
            }
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("========== Practical 9 - Problem 2: Seat Booking Race ==========");

        // Test 1: Unsynchronized (Race condition leading to overselling)
        System.out.println("\n--- 1. Unsynchronized Booking ---");
        UnsafeTheater unsafeTheater = new UnsafeTheater();
        Thread[] unsafeThreads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            final String name = "Passenger-" + (i + 1);
            unsafeThreads[i] = new Thread(() -> {
                boolean booked = unsafeTheater.bookSeat(name);
                if (booked) {
                    System.out.println(name + " successfully booked a seat (Unsafe).");
                }
            });
            unsafeThreads[i].start();
        }
        for (Thread t : unsafeThreads) {
            t.join();
        }
        System.out.println("Initial seats: 5 | Successful bookings: " + unsafeTheater.successfulBookings +
                " | Remaining seats: " + unsafeTheater.seatsLeft +
                (unsafeTheater.successfulBookings > 5 ? " [OVERSELL DETECTED!]" : ""));

        // Test 2: Synchronized (Thread-safe, exactly 5 succeed)
        System.out.println("\n--- 2. Synchronized Booking ---");
        SafeTheater safeTheater = new SafeTheater();
        Thread[] safeThreads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            final String name = "Passenger-" + (i + 1);
            safeThreads[i] = new Thread(() -> {
                boolean booked = safeTheater.bookSeat(name);
                if (booked) {
                    System.out.println(name + " successfully booked a seat (Safe).");
                } else {
                    System.out.println(name + " failed to book: Sold out.");
                }
            });
            safeThreads[i].start();
        }
        for (Thread t : safeThreads) {
            t.join();
        }
        System.out.println("Initial seats: 5 | Successful bookings: " + safeTheater.successfulBookings +
                " | Remaining seats: " + safeTheater.seatsLeft +
                " [EXACTLY 5 BOOKED]");
    }
}
