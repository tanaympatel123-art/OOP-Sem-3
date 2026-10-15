import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class DiscountEngine {
    public static void main(String[] args) {
        List<Double> prices = List.of(100.0, 250.0, 500.0);
        DoubleUnaryOperator discount = price -> price * 0.9;

        prices.forEach(price ->
            System.out.printf("%.2f -> %.2f%n", price, discount.applyAsDouble(price))
        );
    }
}
