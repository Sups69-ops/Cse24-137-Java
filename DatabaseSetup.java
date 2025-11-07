package com.Bank;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Apply SQL files from migrations/ to the local bank.db. */
public class DatabaseSetup {
    // Path where migration SQL files are expected
    private static final Path MIGRATIONS_DIR = Paths.get("migrations");

    public static void main(String[] args) {
        System.out.println("DatabaseSetup: starting - will execute migrations from 'migrations/'");

        // Open a connection to ensure the DB file exists and to run migrations.
        try (Connection conn = DB.getConnection()) {
            // If the migrations directory doesn't exist, inform the user and exit.
            if (!Files.exists(MIGRATIONS_DIR) || !Files.isDirectory(MIGRATIONS_DIR)) {
                System.out.println("No migrations directory found at 'migrations/'. Nothing to do.");
                return;
            }

            // Iterate over all *.sql files and apply them.
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(MIGRATIONS_DIR, "*.sql")) {
                boolean found = false;
                int applied = 0;
                for (Path p : stream) {
                    found = true;
                    System.out.println("Applying migration: " + p.getFileName());
                    String sql = Files.readString(p, StandardCharsets.UTF_8);

                    // Split SQL into statements. This is a simple split and may not
                    // handle complex cases (e.g. semicolons inside strings). For
                    // a production-grade migrator use a dedicated library.
                    String[] statements = sql.split(";\\s*(?=\\r?$|\\r?\\n)");
                    for (String stmt : statements) {
                        String s = stmt.trim();
                        if (s.isEmpty()) continue;
                        try (PreparedStatement ps = conn.prepareStatement(s)) {
                            ps.execute();
                            applied++;
                        } catch (SQLException se) {
                            // Log and continue to allow later statements/files to run
                            System.err.println("Error executing statement from " + p.getFileName() + ": " + se.getMessage());
                        }
                    }
                }
                if (!found) {
                    System.out.println("No migration (.sql) files found in migrations/. Add files and re-run.");
                } else {
                    System.out.println("DatabaseSetup: applied " + applied + " statements from migrations.");
                }
            } catch (IOException ioe) {
                System.err.println("Failed reading migrations directory: " + ioe.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Failed to open database connection: " + e.getMessage());
        }
    }
}
