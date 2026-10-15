import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PackagedApp {

    public static class RecordItem implements Serializable {
        private static final long serialVersionUID = 1L;

        private final int id;
        private final String title;
        private final double value;

        public RecordItem(int id, String title, double value) {
            this.id = id;
            this.title = title;
            this.value = value;
        }

        public int getId() { return id; }
        public String getTitle() { return title; }
        public double getValue() { return value; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RecordItem that)) return false;
            return id == that.id && Double.compare(that.value, value) == 0 && Objects.equals(title, that.title);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, title, value);
        }

        @Override
        public String toString() {
            return "RecordItem[id=" + id + ", title='" + title + "', value=" + value + "]";
        }
    }

    public static class DataStore {
        @SuppressWarnings("unchecked")
        public static List<RecordItem> loadData(Path file) throws IOException, ClassNotFoundException {
            if (!Files.exists(file)) {
                return new ArrayList<>();
            }
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
                return (List<RecordItem>) ois.readObject();
            }
        }

        public static void saveData(Path file, List<RecordItem> items) throws IOException {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file))) {
                oos.writeObject(items);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("========== Practical 14 - Problem 2: Packaged App & Data Persistence ==========");

        Path dataFile = Path.of("packaged_app_data.dat");

        try {
            List<RecordItem> originalItems = List.of(
                    new RecordItem(1, "Server Config", 199.99),
                    new RecordItem(2, "Database Backup", 450.00),
                    new RecordItem(3, "SSL Certificate", 79.50)
            );

            System.out.println("Saving " + originalItems.size() + " items to persistent store...");
            DataStore.saveData(dataFile, originalItems);

            System.out.println("Reloading items from persistent store...");
            List<RecordItem> loadedItems = DataStore.loadData(dataFile);

            System.out.println("Reloaded items:");
            for (RecordItem item : loadedItems) {
                System.out.println("  " + item);
            }

            boolean intact = originalItems.equals(loadedItems);
            System.out.println("Persistence Verification (Unchanged): " + intact);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                Files.deleteIfExists(dataFile); // clean up temporary test file
            } catch (IOException ignored) {}
        }
    }
}
