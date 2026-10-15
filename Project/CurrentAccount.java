class CurrentAccount extends Account {
    private final long overdraftLimit;

    CurrentAccount(
            String ownerName,
            long openingBalance,
            long overdraftLimit
    ) {
        super(ownerName, openingBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double interestRate() {
        return 0.0;
    }

    @Override
    boolean canWithdraw(long amount) {
        return getBalance() - amount >= -overdraftLimit;
    }
}
