import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

public class MiniBankTests {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Test {
    }

    @Test
    public void testDepositIncreasesBalance() throws Exception {
        Account acc = new SavingsAccount("Test Owner", 5000, 1000);
        acc.deposit(2000);
        if (acc.getBalance() != 7000) {
            throw new AssertionError("Expected balance 7000, but got " + acc.getBalance());
        }
    }

    @Test
    public void testWithdrawalOverLimitThrowsException() {
        Account acc = new SavingsAccount("Test Owner", 1000, 500);
        boolean caught = false;
        try {
            acc.withdraw(5000);
        } catch (InsufficientFundsException e) {
            caught = true;
        } catch (Exception e) {
            throw new AssertionError("Expected InsufficientFundsException, but got " + e.getClass().getSimpleName());
        }

        if (!caught) {
            throw new AssertionError("Withdrawal over limit did not throw an exception!");
        }
    }

    @Test
    public void testConcurrentDepositsKeepBalanceCorrect() throws Exception {
        final int NUM_THREADS = 10;
        final int TIMES = 1000;
        final long AMOUNT = 1;
        final long EXPECTED = NUM_THREADS * TIMES * AMOUNT;

        Account acc = new SavingsAccount("Concurrent Owner", 0, 0);
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(new AccountWorker(acc, TIMES, AMOUNT));
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        if (acc.getBalance() != EXPECTED) {
            throw new AssertionError("Expected synchronized balance " + EXPECTED + ", but got " + acc.getBalance());
        }
    }

    @Test
    public void testSaveAndReloadStatePreservesData() throws Exception {
        Path tempFile = Path.of("test_state_persistence.dat");
        try {
            Account[] original = new Account[]{
                    new SavingsAccount("Owner A", 4500, 1000),
                    new CurrentAccount("Owner B", 8000, 2000)
            };

            StatePersister.save(original, tempFile);
            Account[] reloaded = StatePersister.load(tempFile);

            if (reloaded.length != original.length) {
                throw new AssertionError("Reloaded count mismatch");
            }
            if (!reloaded[0].getAccountNumber().equals(original[0].getAccountNumber()) ||
                    reloaded[0].getBalance() != original[0].getBalance()) {
                throw new AssertionError("Account 0 data mismatch after reload");
            }
            if (!reloaded[1].getAccountNumber().equals(original[1].getAccountNumber()) ||
                    reloaded[1].getBalance() != original[1].getBalance()) {
                throw new AssertionError("Account 1 data mismatch after reload");
            }
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    public void testGenericRepositoryReflection() {
        Repository<Account> repo = new Repository<>();
        Account a1 = new SavingsAccount("Repo User", 3000, 500);
        repo.save(a1);

        Account fetched = repo.findById(a1.getAccountNumber());
        if (fetched == null || !fetched.equals(a1)) {
            throw new AssertionError("Repository findById failed to retrieve entity");
        }
        if (repo.size() != 1) {
            throw new AssertionError("Expected repository size 1, but got " + repo.size());
        }
    }

    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("        MINIBANK AUTOMATED UNIT TEST RUNNER (PRACTICAL 14)      ");
        System.out.println("===============================================================");

        MiniBankTests suite = new MiniBankTests();
        Method[] methods = MiniBankTests.class.getDeclaredMethods();
        int total = 0;
        int passed = 0;

        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                total++;
                try {
                    method.invoke(suite);
                    System.out.println("  [PASS] " + method.getName());
                    passed++;
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    System.err.println("  [FAIL] " + method.getName() + " -> " + cause.getMessage());
                }
            }
        }

        System.out.println("---------------------------------------------------------------");
        System.out.println("Results: " + passed + " / " + total + " tests passed successfully.");
        System.out.println("Status: " + (passed == total ? "ALL TESTS PASSED (100%)" : "SOME TESTS FAILED"));
        System.out.println("===============================================================");
    }
}
