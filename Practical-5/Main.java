import java.util.*;

abstract class Media {
    protected String title;
    protected int lateDays;

    Media(String title, int lateDays) {
        this.title = title;
        this.lateDays = lateDays;
    }

    abstract double calculateLateFee();
}

class Book extends Media {
    Book(String title, int lateDays) {
        super(title, lateDays);
    }

    double calculateLateFee() {
        return lateDays * 2.0;
    }
}

class DVD extends Media {
    DVD(String title, int lateDays) {
        super(title, lateDays);
    }

    double calculateLateFee() {
        return lateDays * 5.0;
    }
}

class Magazine extends Media {
    Magazine(String title, int lateDays) {
        super(title, lateDays);
    }

    double calculateLateFee() {
        return lateDays * 1.5;
    }
}

public class Main {
    public static void main(String[] args) {

        ArrayList<Media> returnedBatch = new ArrayList<>();

        returnedBatch.add(new Book("Java Programming", 3));
        returnedBatch.add(new DVD("Avengers", 4));
        returnedBatch.add(new Magazine("Tech Today", 2));
        returnedBatch.add(new Book("Data Structures", 5));

        double totalFee = 0;

        for (Media media : returnedBatch) {
            double fee = media.calculateLateFee();

            System.out.println(
                media.title + " - Late Days: " +
                media.lateDays + " - Fee: ₹" + fee
            );

            totalFee += fee;
        }

        System.out.println("----------------------------");
        System.out.println("Total Late Fee: ₹" + totalFee);
    }
}