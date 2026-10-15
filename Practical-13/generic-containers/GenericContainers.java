public class GenericContainers {

    // Generic Box<T>
    public static class Box<T> {
        private T value;

        public Box() {
        }

        public Box(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }

        public boolean isEmpty() {
            return value == null;
        }

        @Override
        public String toString() {
            return "Box[" + value + "]";
        }
    }

    // Generic Pair<A, B>
    public static class Pair<A, B> {
        private final A first;
        private final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A first() {
            return first;
        }

        public B second() {
            return second;
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }
    }

    public static void main(String[] args) {
        System.out.println("========== Practical 13 - Problem 1: Generic Containers (Box & Pair) ==========");

        // Box with String
        Box<String> stringBox = new Box<>("Hello, Generics!");
        System.out.println("String Box: " + stringBox.get());
        stringBox.set("Updated Greeting");
        System.out.println("Updated String Box: " + stringBox.get());

        // Box with Integer
        Box<Integer> intBox = new Box<>(100);
        System.out.println("Integer Box value: " + intBox.get());

        // Pair with String and Double
        Pair<String, Double> productPrice = new Pair<>("Java Programming Book", 49.99);
        System.out.println("Product: " + productPrice.first() + " | Price: Rs. " + productPrice.second());

        // Pair with Integer and String
        Pair<Integer, String> studentEntry = new Pair<>(90, "Tanay Patel");
        System.out.println("Student ID: " + studentEntry.first() + " -> " + studentEntry.second());
    }
}
