package com.Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Simple DAO for recording transactions to the transactions table. */
public class TransactionDAO {

    /** Record a transaction row (accountNumber, type, amount). */
    public static void createTransaction(String accountNumber, String type, double amount) throws SQLException {
        String sql = "INSERT INTO transactions(account_number, type, amount) VALUES (?,?,?)";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.executeUpdate();
        }
    }
}
