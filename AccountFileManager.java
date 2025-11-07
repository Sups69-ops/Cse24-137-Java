package com.Bank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AccountFileManager {
    private static final String ACCOUNTS_FILE = "accounts.txt";

    /**
     * Generate a new account number for the given account type.
     * Formats:
     *  - SAVINGS -> SV01, SV02, ...
     *  - INVESTMENTS -> IV01, IV02, ...
     *  - CHEQUE -> CQ01, CQ02, ...
     */
    public static String generateAccountNumber(String accountType) throws IOException {
        String prefix = "AC";
        if ("SAVINGS".equalsIgnoreCase(accountType)) prefix = "SV";
        else if ("INVESTMENTS".equalsIgnoreCase(accountType)) prefix = "IV";
        else if ("CHEQUE".equalsIgnoreCase(accountType) || "CHECK".equalsIgnoreCase(accountType)) prefix = "CQ";

        List<Account> accounts = getAllAccounts();
        int max = 0;
        for (Account a : accounts) {
            String num = a.getAccountNumber();
            if (num != null && num.startsWith(prefix)) {
                String tail = num.substring(prefix.length());
                try {
                    int v = Integer.parseInt(tail);
                    if (v > max) max = v;
                } catch (NumberFormatException e) {
                    // ignore non-numeric tails
                }
            }
        }
        int next = max + 1;
        // Pad with three digits (001, 002, ...)
        String padded = String.format("%03d", next);
        return prefix + padded;
    }

    public static void updateAccount(Account updatedAccount) throws IOException {
        List<Account> accounts = getAllAccounts();
        List<Account> updated = new ArrayList<>();
        
        boolean found = false;
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(updatedAccount.getAccountNumber())) {
                updated.add(updatedAccount);
                found = true;
            } else {
                updated.add(account);
            }
        }
        
        if (!found) {
            updated.add(updatedAccount);
        }
        
        saveAllAccounts(updated);
    }

    public static List<Account> getAllAccounts() throws IOException {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    accounts.add(Account.fromFileString(line));
                }
            }
        }
        return accounts;
    }

    private static void saveAllAccounts(List<Account> accounts) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (Account account : accounts) {
                writer.println(account.toFileString());
            }
        }
    }
}