package com.Bank;

public class SavingsAccount extends Account {
    public SavingsAccount() {
        super("", "", 0.0, "SAVINGS", "");
    }
    
    public SavingsAccount(String accountNumber, String customerId, double balance, String branch) {
        super(accountNumber, customerId, balance, "SAVINGS", branch);
    }
}
