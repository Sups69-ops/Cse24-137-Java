package com.Bank;

import java.sql.Connection;
import java.sql.SQLException;

/** Simple utility that opens a connection to bank.db (creates the file if needed). */
public class CreateDatabase {
    public static void main(String[] args) {
        System.out.println("CreateDatabase: opening connection to bank.db");
        try (Connection c = DB.getConnection()) {
            // Opening a Connection creates bank.db on disk for SQLite if missing.
            System.out.println("CreateDatabase: bank.db is ready (connection successful)");
        } catch (SQLException e) {
            System.err.println("CreateDatabase failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }
}
