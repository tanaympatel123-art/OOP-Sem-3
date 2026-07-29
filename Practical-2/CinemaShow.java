public class CinemaShow {

    // Instance Variables
    private String title;
    private int seatsAvailable;
    private final int capacity;

    // Static Variable
    private static int totalBooked = 0;

    // Constructor with title and capacity
    public CinemaShow(String title, int capacity) {
        this.title = title;
        this.capacity = capacity;
        this.seatsAvailable = capacity;
    }

    // Constructor with only title (Constructor Chaining)
    public CinemaShow(String title) {
        this(title, 100);
    }

    // Book seats
    public boolean book(int n) {
        if (n <= seatsAvailable) {
            seatsAvailable -= n;
            totalBooked += n;
            return true;
        } else {
            return false;
        }
    }

    // Cancel seats
    public void cancel(int n) {
        seatsAvailable += n;

        if (seatsAvailable > capacity) {
            seatsAvailable = capacity;
        }
    }

    // Getter for available seats
    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    // Static Getter for total booked seats
    public static int getTotalBooked() {
        return totalBooked;
    }

    // Main Method
    public static void main(String[] args) {

        CinemaShow show = new CinemaShow("Avengers", 50);

        // Book 20 seats
        System.out.println("Book 20: " + show.book(20));
        System.out.println("Seats Available: " + show.getSeatsAvailable());

        // Book 15 seats
        System.out.println("Book 15: " + show.book(15));
        System.out.println("Seats Available: " + show.getSeatsAvailable());

        // Try booking more seats than available
        System.out.println("Book 30: " + show.book(30));
        System.out.println("Seats Available: " + show.getSeatsAvailable());

        // Cancel 10 seats
        show.cancel(10);
        System.out.println("After cancelling 10 seats:");
        System.out.println("Seats Available: " + show.getSeatsAvailable());

        // Cancel 50 seats (should not exceed capacity)
        show.cancel(50);
        System.out.println("After cancelling 50 seats:");
        System.out.println("Seats Available: " + show.getSeatsAvailable());

        // Print total successful bookings
        System.out.println("Total Booked Seats: " + CinemaShow.getTotalBooked());
    }
}