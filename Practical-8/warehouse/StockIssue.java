import java.util.ArrayList;
import java.util.List;

class OutOfStockException extends Exception {

    private int shortfall;

    public OutOfStockException(String message, int shortfall) {
        super(message);
        this.shortfall = shortfall;
    }

    public int getShortfall() {
        return shortfall;
    }
}

class InvalidQuantityException extends Exception {

    public InvalidQuantityException(String message) {
        super(message);
    }
}

class Warehouse {

    private int stock;

    public Warehouse(int stock) {
        this.stock = stock;
    }

    public void issue(String item, int qty)
            throws OutOfStockException, InvalidQuantityException {

        if (qty <= 0) {
            throw new InvalidQuantityException(
                    "Quantity must be greater than zero"
            );
        }

        if (qty > stock) {
            int shortfall = qty - stock;

            throw new OutOfStockException(
                    "Not enough stock for " + item,
                    shortfall
            );
        }

        stock -= qty;

        System.out.println(
                "Issued " + qty + " units of " + item
        );

        System.out.println(
                "Remaining stock: " + stock
        );
    }
}

class Request {

    String item;
    int quantity;

    Request(String item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }
}

public class StockIssue {

    public static void main(String[] args) {

        Warehouse warehouse = new Warehouse(10);

        List<Request> requests = new ArrayList<>();

        requests.add(new Request("Laptop", 4));
        requests.add(new Request("Mouse", 0));
        requests.add(new Request("Keyboard", 8));
        requests.add(new Request("Monitor", -2));
        requests.add(new Request("Printer", 5));

        for (Request request : requests) {

            try {

                warehouse.issue(
                        request.item,
                        request.quantity
                );

            } catch (OutOfStockException e) {

                System.out.println(
                        "Out of stock: " + e.getMessage()
                );

                System.out.println(
                        "Shortfall: " + e.getShortfall()
                );

            } catch (InvalidQuantityException e) {

                System.out.println(
                        "Invalid quantity: " + e.getMessage()
                );
            }

            System.out.println();
        }

        System.out.println("All requests processed.");
    }
}