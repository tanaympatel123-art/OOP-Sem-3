class FileResource implements AutoCloseable {

    public FileResource() {
        System.out.println("Resource opened");
    }

    public void readData() {
        System.out.println("Reading data...");
    }

    @Override
    public void close() {
        System.out.println("Resource closed");
    }
}

public class AutoCloseableDemo {

    public static void main(String[] args) {

        try (FileResource resource = new FileResource()) {

            resource.readData();

            System.out.println("Throwing an exception...");
            throw new RuntimeException("Original error inside resource");

        } catch (Exception e) {

            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("Program continues.");
    }
}