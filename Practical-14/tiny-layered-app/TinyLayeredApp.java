import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TinyLayeredApp {

    // Test Annotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Test {}

    // Model Layer
    public static class Product {
        private final int id;
        private final String name;
        private final double price;

        public Product(int id, String name, double price) {
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative: " + price);
            }
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public double getPrice() { return price; }

        @Override
        public String toString() {
            return "Product[id=" + id + ", name='" + name + "', price=" + price + "]";
        }
    }

    // Service Layer
    public static class ProductService {
        private final List<Product> catalog = new ArrayList<>();

        public void addProduct(Product product) {
            if (product == null) {
                throw new IllegalArgumentException("Product cannot be null");
            }
            for (Product p : catalog) {
                if (p.getId() == product.getId()) {
                    throw new IllegalStateException("Product with ID " + product.getId() + " already exists");
                }
            }
            catalog.add(product);
        }

        public Product getProduct(int id) {
            for (Product p : catalog) {
                if (p.getId() == id) {
                    return p;
                }
            }
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }

        public int getCount() {
            return catalog.size();
        }

        public List<Product> getAllProducts() {
            return new ArrayList<>(catalog);
        }
    }

    // Test Suite
    public static class ProductServiceTests {
        private final ProductService service = new ProductService();

        @Test
        public void testAddProductIncreasesCount() {
            int before = service.getCount();
            service.addProduct(new Product(1, "Mechanical Keyboard", 89.99));
            int after = service.getCount();
            if (after != before + 1) {
                throw new AssertionError("Expected count " + (before + 1) + " but got " + after);
            }
            System.out.println("  PASS: testAddProductIncreasesCount");
        }

        @Test
        public void testNegativePriceThrowsException() {
            try {
                new Product(2, "Invalid Item", -25.0);
                throw new AssertionError("Expected IllegalArgumentException for negative price");
            } catch (IllegalArgumentException e) {
                System.out.println("  PASS: testNegativePriceThrowsException (" + e.getMessage() + ")");
            }
        }

        @Test
        public void testMissingProductThrowsException() {
            try {
                service.getProduct(999);
                throw new AssertionError("Expected IllegalArgumentException for missing product");
            } catch (IllegalArgumentException e) {
                System.out.println("  PASS: testMissingProductThrowsException (" + e.getMessage() + ")");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("========== Practical 14 - Problem 1: Tiny Layered App & Unit Tests ==========");

        // Run automated tests using reflection
        System.out.println("\n--- Running Unit Tests ---");
        ProductServiceTests testSuite = new ProductServiceTests();
        int testsRun = 0;
        int testsPassed = 0;

        for (Method method : ProductServiceTests.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                testsRun++;
                try {
                    method.invoke(testSuite);
                    testsPassed++;
                } catch (Exception e) {
                    System.err.println("  FAIL: " + method.getName() + " -> " + e.getCause().getMessage());
                }
            }
        }
        System.out.println("Test Execution Summary: " + testsPassed + "/" + testsRun + " passed.");

        // Interactive Layer demonstration
        System.out.println("\n--- Service & Model Layer Demonstration ---");
        ProductService service = new ProductService();
        service.addProduct(new Product(101, "Java SE Cookbook", 45.00));
        service.addProduct(new Product(102, "USB-C Hub", 29.50));

        System.out.println("Total products in catalog: " + service.getCount());
        for (Product p : service.getAllProducts()) {
            System.out.println("  " + p);
        }
    }
}
