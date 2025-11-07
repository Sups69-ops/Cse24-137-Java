package com.Bank;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDB {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            System.out.println("✅ Connection successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
