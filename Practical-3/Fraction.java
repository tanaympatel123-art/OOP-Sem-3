import java.util.Objects;

public class Fraction {

    private int num;
    private int den;

    // Constructor
    public Fraction(int num, int den) {

        if (den == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero.");
        }

        int g = gcd(num, den);

        this.num = num / g;
        this.den = den / g;

        // Keep denominator positive
        if (this.den < 0) {
            this.num = -this.num;
            this.den = -this.den;
        }
    }

    // Method to find GCD
    private int gcd(int a, int b) {

        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }

    // Override toString()
    @Override
    public String toString() {
        return num + "/" + den;
    }

    // Override equals()
    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Fraction other = (Fraction) obj;

        return num == other.num && den == other.den;
    }

    // Override hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(num, den);
    }
}