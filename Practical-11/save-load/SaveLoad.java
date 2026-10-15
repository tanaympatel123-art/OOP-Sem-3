import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class SaveLoad {

    static class Student implements Serializable {
        private static final long serialVersionUID = 1L;

        private final int id;
        private final String name;
        private final double gpa;

        public Student(int id, String name, double gpa) {
            this.id = id;
            this.name = name;
            this.gpa = gpa;
        }

        @Override
        public String toString() {
            return "Student{id=" + id + ", name='" + name + "', gpa=" + gpa + "}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Student s)) return false;
            return id == s.id && Double.compare(s.gpa, gpa) == 0 && name.equals(s.name);
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(id);
        }
    }

    public static void main(String[] args) {
        System.out.println("========== Practical 11 - Problem 1: Object Serialization (Save/Load) ==========");

        Student[] originalStudents = new Student[]{
                new Student(101, "Tanay", 9.4),
                new Student(102, "Aarav", 8.9),
                new Student(103, "Diya", 9.6)
        };

        Path dataFile = Path.of("students_temp.dat");

        // Save
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(dataFile))) {
            oos.writeObject(originalStudents);
            System.out.println("Saved " + originalStudents.length + " students to " + dataFile.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving: " + e.getMessage());
        }

        // Load
        Student[] loadedStudents = null;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(dataFile))) {
            loadedStudents = (Student[]) ois.readObject();
            System.out.println("Loaded " + loadedStudents.length + " students from " + dataFile.getFileName());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading: " + e.getMessage());
        } finally {
            try {
                Files.deleteIfExists(dataFile); // clean up temporary file
            } catch (IOException ignored) {}
        }

        if (loadedStudents != null) {
            System.out.println("Loaded students:");
            for (Student s : loadedStudents) {
                System.out.println("  " + s);
            }
            boolean matches = Arrays.equals(originalStudents, loadedStudents);
            System.out.println("Integrity Check (Matches Original): " + matches);
        }
    }
}
