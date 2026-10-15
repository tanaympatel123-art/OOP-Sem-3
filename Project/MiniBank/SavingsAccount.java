class SavingsAccount extends Account implements Premium {
    private final long minimumBalance;

    SavingsAccount(
            String ownerName,
            long openingBalance,
            long minimumBalance
    ) {
        super(ownerName, openingBalance);
        this.minimumBalance = minimumBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    boolean canWithdraw(long amount) {
        return getBalance() - amount >= minimumBalance;
    }
}
