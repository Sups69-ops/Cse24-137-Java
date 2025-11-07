package com.Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Simple helper that returns JDBC Connections to the local bank.db file. */
public class DB {
    // JDBC URL for the SQLite database file located in project root
    private static final String URL = "jdbc:sqlite:bank.db";

    // Load the SQLite JDBC driver at class initialization for clearer errors.
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // Rethrow as an unchecked error so startup fails loudly when driver absent
            throw new ExceptionInInitializerError(e);
        }
    }

    /** Return a new JDBC Connection to bank.db. Caller should close it. */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
