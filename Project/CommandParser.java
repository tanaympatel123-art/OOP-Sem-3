class CommandParser {
    private CommandParser() {
    }

    static Command parse(String line) {
        String[] parts = line.trim().split("\\s+");

        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Command format: TYPE ACCOUNT_NUMBER AMOUNT"
            );
        }

        TransactionType type = TransactionType.valueOf(
                parts[0].toUpperCase()
        );
        long amount = Long.parseLong(parts[2]);

        return new Command(type, parts[1], amount);
    }
}
