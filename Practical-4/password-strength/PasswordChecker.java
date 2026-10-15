import java.util.List;

public class PasswordChecker {
    public static String strength(String password) {
        int passed = 0;
        if (password.length() >= 8) passed++;
        if (password.matches(".*[A-Z].*")) passed++;
        if (password.matches(".*\\d.*")) passed++;
        if (password.matches(".*[^A-Za-z0-9].*")) passed++;
        return passed <= 1 ? "Weak" : passed <= 3 ? "Medium" : "Strong";
    }

    public static void main(String[] args) {
        for (String password : List.of("abc", "Abcd1234", "Abcd1234!")) {
            System.out.printf("%s -> %s%n", password, strength(password));
        }
    }
}
