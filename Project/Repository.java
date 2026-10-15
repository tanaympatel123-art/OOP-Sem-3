import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Repository<T> {
    private final Map<String, T> store = new HashMap<>();

    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        String id = extractId(entity);
        if (id == null) {
            throw new IllegalStateException("Could not extract ID for entity of type " + entity.getClass().getName());
        }

        store.put(id, entity);
        return entity;
    }

    public T findById(String id) {
        return store.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(store.values());
    }

    public boolean delete(String id) {
        return store.remove(id) != null;
    }

    public int size() {
        return store.size();
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        if (items != null) {
            for (T item : items) {
                save(item);
            }
        }
    }

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            return 0.0;
        }
        double sum = 0.0;
        for (N val : values) {
            sum += val.doubleValue();
        }
        return sum / values.length;
    }

    private String extractId(T entity) {
        // 1. Search for field annotated with @Id in the class and its superclasses
        Class<?> clazz = entity.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        Object val = field.get(entity);
                        if (val != null) {
                            return val.toString();
                        }
                    } catch (Exception ignored) {}
                }
            }
            clazz = clazz.getSuperclass();
        }

        // 2. Fallback: Check getter methods or fields named id / accountNumber / customerId
        try {
            Method m = entity.getClass().getMethod("getAccountNumber");
            return String.valueOf(m.invoke(entity));
        } catch (Exception ignored) {}

        try {
            Method m = entity.getClass().getMethod("getCustomerId");
            return String.valueOf(m.invoke(entity));
        } catch (Exception ignored) {}

        try {
            Field f = entity.getClass().getDeclaredField("customerId");
            f.setAccessible(true);
            return String.valueOf(f.get(entity));
        } catch (Exception ignored) {}

        return null;
    }

    public static void main(String[] args) {
        System.out.println("========== MiniBank Part B - Practical 13: Generic Repository ==========");

        // 1. Repository<Account>
        Repository<Account> accountRepo = new Repository<>();

        Account acc1 = new SavingsAccount("Tanay Patel", 5000, 1000);
        Account acc2 = new CurrentAccount("Alice Johnson", 12000, 2000);
        Account acc3 = new FixedDepositAccount("Bob Smith", 25000);

        accountRepo.saveAll(acc1, acc2, acc3);

        System.out.println("--- 1. findById Test ---");
        Account found = accountRepo.findById(acc2.getAccountNumber());
        System.out.println("findById(\"" + acc2.getAccountNumber() + "\") returned: " + found);

        System.out.println("\n--- 2. findAll Test (" + accountRepo.size() + " accounts) ---");
        ArrayList<Account> allAccounts = accountRepo.findAll();
        for (Account a : allAccounts) {
            System.out.println("  * " + a);
        }

        // 2. Reuse generic repository for Customer objects
        System.out.println("\n--- 3. Reusing Repository<Customer> for Customer entities ---");
        Repository<Customer> customerRepo = new Repository<>();
        Customer c1 = new Customer("Tanay Patel", "tanay@charusat.edu", "9876543210",
                new Customer.Address("123 Main St", "Anand", "388001"));
        Customer c2 = new Customer("Alice Johnson", "alice@test.com", "9123456780",
                new Customer.Address("456 Park Ave", "Vadodara", "390001"));

        customerRepo.saveAll(c1, c2);
        System.out.println("Saved " + customerRepo.size() + " customers.");
        Customer foundCustomer = customerRepo.findById(c1.getCustomerId());
        System.out.println("Found customer by ID: " + foundCustomer.getName() + " (" + foundCustomer.getEmail() + ")");

        // 3. Bounded type parameter average calculation
        System.out.println("\n--- 4. Bounded Type Generic Method: average() ---");
        Double[] balances = {5000.0, 12000.0, 25000.0};
        System.out.printf("Average Account Balance: Rs. %.2f\n", Repository.average(balances));
    }
}
