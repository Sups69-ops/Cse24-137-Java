package com.Bank;

/** Simple model for a bank account. Can be saved to and loaded from a CSV line. */
public class Account {
    private String accountNumber;
    private String customerId;
    private double balance;
    private String accountType;
    private String branch;

    public Account(String accountNumber, String customerId, double balance, String accountType, String branch) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = balance;
        this.accountType = accountType;
        this.branch = branch;
    }

    /** Convert this account into a CSV line for storage. */
    public String toFileString() {
        return String.format("%s,%s,%.2f,%s,%s",
            accountNumber, customerId, balance, accountType, branch);
    }

    /** Parse a CSV line and return an Account (accountNumber,customerId,balance,accountType,branch). */
    public static Account fromFileString(String line) {
        String[] parts = line.split(",");
        return new Account(
            parts[0],
            parts[1],
            Double.parseDouble(parts[2]),
            parts[3],
            parts[4]
        );
    }

    public String getAccountNumber() { return accountNumber; }
    public String getCustomerId() { return customerId; }
    public double getBalance() { return balance; }
    public String getAccountType() { return accountType; }
    public String getBranch() { return branch; }
}
