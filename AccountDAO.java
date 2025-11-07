package com.Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Simple DAO for accounts: create, list by customer, update, and generate IDs. */
public class AccountDAO {

    /** Insert a new account row. */
    public static void createAccount(Account acc) throws SQLException {
        String sql = "INSERT INTO accounts(account_number, customer_id, balance, account_type, branch) VALUES (?,?,?,?,?)";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, acc.getAccountNumber());
            ps.setString(2, acc.getCustomerId());
            ps.setDouble(3, acc.getBalance());
            ps.setString(4, acc.getAccountType());
            ps.setString(5, acc.getBranch());
            ps.executeUpdate();
        }
    }

    /** Return accounts for the given customer. */
    public static List<Account> getAccountsForCustomer(String customerId) throws SQLException {
        String sql = "SELECT account_number, customer_id, balance, account_type, branch FROM accounts WHERE customer_id = ?";
        List<Account> out = new ArrayList<>();
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Account(
                        rs.getString("account_number"),
                        rs.getString("customer_id"),
                        rs.getDouble("balance"),
                        rs.getString("account_type"),
                        rs.getString("branch")
                    ));
                }
            }
        }
        return out;
    }

    /** Update account balance/type/branch. */
    public static void updateAccount(Account acc) throws SQLException {
        String sql = "UPDATE accounts SET balance = ?, account_type = ?, branch = ? WHERE account_number = ?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, acc.getBalance());
            ps.setString(2, acc.getAccountType());
            ps.setString(3, acc.getBranch());
            ps.setString(4, acc.getAccountNumber());
            ps.executeUpdate();
        }
    }

    /**
     * Generate a new account number. Uses prefixes (SV/IV/CQ) and a 3-digit suffix.
     */
    public static String generateAccountNumber(String accountType) throws SQLException {
        String prefix = "SV";
        if (accountType != null) {
            String t = accountType.toLowerCase();
            if (t.contains("invest")) prefix = "IV";
            else if (t.contains("cheque") || t.contains("check")) prefix = "CQ";
        }

        String sql = "SELECT account_number FROM accounts WHERE account_number LIKE ?";
        int max = 0;
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String acc = rs.getString(1);
                    if (acc.length() > 2) {
                        String num = acc.substring(2);
                        try {
                            int v = Integer.parseInt(num);
                            if (v > max) max = v;
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
        }
        int next = max + 1;
        return String.format(prefix + "%03d", next);
    }
}
