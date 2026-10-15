import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BankService {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Audit {
        String action() default "";
    }

    private final Repository<Account> repository;
    private final Path logFile = Path.of("data/logs/minibank.log");

    public BankService() {
        this(new Repository<>());
    }

    public BankService(Repository<Account> repository) {
        this.repository = repository;
    }

    public Repository<Account> getRepository() {
        return repository;
    }

    @Audit(action = "OPEN_ACCOUNT")
    public Account openAccount(String type, String ownerName, long initialBalance, long extraParam) throws BankException {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new BankException("Owner name cannot be blank.");
        }
        if (initialBalance < 0) {
            throw new InvalidAmountException();
        }

        Account account;
        String cleanType = type != null ? type.trim().toLowerCase() : "savings";
        switch (cleanType) {
            case "current" -> account = new CurrentAccount(ownerName.trim(), initialBalance, extraParam > 0 ? extraParam : 1000);
            case "fixed", "fd", "fixeddeposit" -> account = new FixedDepositAccount(ownerName.trim(), initialBalance);
            default -> account = new SavingsAccount(ownerName.trim(), initialBalance, extraParam > 0 ? extraParam : 500);
        }

        repository.save(account);
        log("OPEN " + account.getAccountNumber() + " " + initialBalance);
        return account;
    }

    @Audit(action = "DEPOSIT")
    public void deposit(String accountNumber, long amount) throws BankException {
        Account account = getAccountOrThrow(accountNumber);
        account.deposit(amount);
        log("DEPOSIT " + accountNumber + " " + amount);
    }

    @Audit(action = "WITHDRAW")
    public void withdraw(String accountNumber, long amount) throws BankException {
        Account account = getAccountOrThrow(accountNumber);
        account.withdraw(amount);
        log("WITHDRAW " + accountNumber + " " + amount);
    }

    @Audit(action = "TRANSFER")
    public void transfer(String fromNumber, String toNumber, long amount) throws BankException {
        Account from = getAccountOrThrow(fromNumber);
        Account to = getAccountOrThrow(toNumber);

        // Safe transfer preventing deadlocks using lock ordering
        TransactionProcessor.safeTransfer(from, to, amount);
        log("WITHDRAW " + fromNumber + " " + amount);
        log("DEPOSIT " + toNumber + " " + amount);
    }

    @Audit(action = "STATEMENT")
    public String statement(String accountNumber) throws AccountNotFoundException {
        Account account = getAccountOrThrow(accountNumber);
        return StatementFormatter.buildStatement(account);
    }

    public List<Account> listAccounts() {
        return repository.findAll();
    }

    public List<Account> listAccountsSortedByBalance() {
        ArrayList<Account> list = repository.findAll();
        list.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return list;
    }

    private Account getAccountOrThrow(String accountNumber) throws AccountNotFoundException {
        Account account = repository.findById(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    private void log(String line) {
        try {
            TransactionLog.append(logFile, line);
        } catch (Exception ignored) {}
    }
}
