class StatementFormatter {
    private StatementFormatter() {
    }

    static String buildStatement(Account account) {
        StringBuilder statement = new StringBuilder();

        statement.append("========== ACCOUNT STATEMENT ==========").append('\n');
        statement.append("Account Number : ")
                .append(account.getAccountNumber()).append('\n');
        statement.append("Owner          : ")
                .append(account.getOwnerName()).append('\n');
        statement.append("Balance        : Rs. ")
                .append(account.getBalance()).append('\n');
        statement.append("Interest Rate  : ")
                .append(account.interestRate()).append('%').append('\n');
        statement.append("Active         : ")
                .append(account.isActive());

        return statement.toString();
    }
}
