import java.util.Objects;

abstract class Account implements Transactable, InterestBearing {
    private static long accountCounter = 1;

    @Id
    private final String accountNumber;

    @MaxLength(40)
    private final String ownerName;

    @Positive
    private long balance;

    private boolean active;

    Account(String ownerName, long openingBalance) {
        this.accountNumber = String.format("AC%04d", accountCounter++);
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    @Override
    public synchronized void deposit(long amount)
            throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException();
        }
        balance += amount;
    }

    @Override
    public synchronized void withdraw(long amount)
            throws BankException {
        if (amount <= 0) {
            throw new InvalidAmountException();
        }
        if (!canWithdraw(amount)) {
            throw new InsufficientFundsException(
                    Math.max(0, amount - balance)
            );
        }
        balance -= amount;
    }

    public void transfer(Account receiver, long amount)
            throws BankException {
        try {
            withdraw(amount);
            receiver.deposit(amount);
        } catch (BankException exception) {
            throw exception;
        } finally {
            System.out.println("Transfer attempt completed.");
        }
    }

    abstract boolean canWithdraw(long amount);

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | Rs. " + balance;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Account account)) {
            return false;
        }
        return accountNumber.equals(account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}
