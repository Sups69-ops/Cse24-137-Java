package com.Bank;

public class ChequeAccount extends Account {
    public ChequeAccount() {
        super("", "", 0.0, "CHEQUE", "");
    }
    
    public ChequeAccount(String accountNumber, String customerId, double balance, String branch) {
        super(accountNumber, customerId, balance, "CHEQUE", branch);
    }
}
