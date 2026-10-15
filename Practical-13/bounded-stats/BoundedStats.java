public class BoundedStats {

    // Generic method with bounded type parameter <N extends Number>
    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            return 0.0;
        }
        double sum = 0.0;
        for (N num : values) {
            sum += num.doubleValue();
        }
        return sum / values.length;
    }

    // Generic method with multiple bounds <N extends Number & Comparable<N>>
    public static <N extends Number & Comparable<N>> N findMax(N[] values) {
        if (values == null || values.length == 0) {
            return null;
        }
        N max = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i].compareTo(max) > 0) {
                max = values[i];
            }
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println("========== Practical 13 - Problem 2: Bounded Type Parameters ==========");

        Integer[] intScores = {85, 92, 78, 96, 88};
        Double[] doubleBalances = {5000.50, 12500.75, 3200.00, 8900.25};
        Long[] longTransactions = {1000L, 50000L, 25000L, 75000L};

        System.out.printf("Integer Array Average      : %.2f | Max: %d\n",
                average(intScores), findMax(intScores));

        System.out.printf("Double Array Average       : %.2f | Max: %.2f\n",
                average(doubleBalances), findMax(doubleBalances));

        System.out.printf("Long Array Average         : %.2f | Max: %d\n",
                average(longTransactions), findMax(longTransactions));
    }
}
