package com.Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Simple DAO for the users table (save/find/validate). */
public class UserDAO {

    /** Save or update a user record. */
    public static void saveUser(User user) throws SQLException {
        String sql = "INSERT OR REPLACE INTO users(customer_id, name, password_hash, address, phone) VALUES (?,?,?,?,?)";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.getCustomerId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getPhone());
            ps.executeUpdate();
        }
    }

    /** Find a user by customer ID, or null if not found. */
    public static User findById(String customerId) throws SQLException {
        String sql = "SELECT customer_id, name, password_hash, address, phone FROM users WHERE customer_id = ?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new User(
                    rs.getString("customer_id"),
                    rs.getString("name"),
                    rs.getString("password_hash"),
                    rs.getString("address"),
                    rs.getString("phone")
                );
            }
        }
    }

    /**
     * Validate credentials (plain-text comparison for now).
     * Replace with hashed passwords for production.
     */
    public static boolean validateUser(String customerId, String password) throws SQLException {
        User u = findById(customerId);
        if (u == null) return false;
        // plain-text comparison for now
        return password != null && password.equals(u.getPassword());
    }
}
