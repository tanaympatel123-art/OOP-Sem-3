interface Transactable {
    void deposit(long amount) throws InvalidAmountException;

    void withdraw(long amount) throws BankException;
}

interface InterestBearing {
    double interestRate();

    default double yearlyInterest(Account account) {
        return account.getBalance() * interestRate() / 100.0;
    }
}

@FunctionalInterface
interface WithdrawRule {
    boolean allow(Account account, long amount);
}

interface Premium {
}
