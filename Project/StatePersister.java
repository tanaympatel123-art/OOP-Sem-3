import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

public class StatePersister {

    static class AccountState implements Serializable {
        private static final long serialVersionUID = 1L;

        final String type;
        final String accountNumber;
        final String ownerName;
        final long balance;
        final boolean active;
        final long extraParam;

        AccountState(String type, String accountNumber, String ownerName, long balance, boolean active, long extraParam) {
            this.type = type;
            this.accountNumber = accountNumber;
            this.ownerName = ownerName;
            this.balance = balance;
            this.active = active;
            this.extraParam = extraParam;
        }
    }

    public static void save(Account[] accounts, Path file) throws IOException {
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }

        AccountState[] states = new AccountState[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            Account acc = accounts[i];
            String type = acc.getClass().getSimpleName();
            long extra = 0;
            try {
                if (acc instanceof SavingsAccount) {
                    Field f = SavingsAccount.class.getDeclaredField("minimumBalance");
                    f.setAccessible(true);
                    extra = f.getLong(acc);
                } else if (acc instanceof CurrentAccount) {
                    Field f = CurrentAccount.class.getDeclaredField("overdraftLimit");
                    f.setAccessible(true);
                    extra = f.getLong(acc);
                }
            } catch (Exception ignored) {}

            states[i] = new AccountState(type, acc.getAccountNumber(), acc.getOwnerName(), acc.getBalance(), acc.isActive(), extra);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file))) {
            oos.writeObject(states);
        }
    }

    public static Account[] load(Path file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
            AccountState[] states = (AccountState[]) ois.readObject();
            Account[] accounts = new Account[states.length];

            for (int i = 0; i < states.length; i++) {
                AccountState s = states[i];
                Account acc;
                if ("CurrentAccount".equals(s.type)) {
                    acc = new CurrentAccount(s.ownerName, s.balance, s.extraParam);
                } else if ("FixedDepositAccount".equals(s.type)) {
                    acc = new FixedDepositAccount(s.ownerName, s.balance);
                } else {
                    acc = new SavingsAccount(s.ownerName, s.balance, s.extraParam);
                }

                try {
                    Field numField = Account.class.getDeclaredField("accountNumber");
                    numField.setAccessible(true);
                    numField.set(acc, s.accountNumber);

                    Field activeField = Account.class.getDeclaredField("active");
                    activeField.setAccessible(true);
                    activeField.setBoolean(acc, s.active);
                } catch (Exception ignored) {}

                accounts[i] = acc;
            }
            return accounts;
        }
    }
}
