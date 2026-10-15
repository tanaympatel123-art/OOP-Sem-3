class BankException extends Exception {
    BankException(String message) {
        super(message);
    }
}

class InvalidAmountException extends BankException {
    InvalidAmountException() {
        super("Amount must be greater than zero.");
    }
}

class InsufficientFundsException extends BankException {
    private final long shortfall;

    InsufficientFundsException(long shortfall) {
        super("Insufficient funds. Shortfall: " + shortfall);
        this.shortfall = shortfall;
    }

    long getShortfall() {
        return shortfall;
    }
}

class AccountNotFoundException extends BankException {
    AccountNotFoundException(String accountNumber) {
        super("Account not found: " + accountNumber);
    }
}
