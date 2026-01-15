package com.example.examples;

import com.example.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Ezénplu 01: Konekta ku bazi di dadus
 *
 * Demonstra:
 * - DriverManager.getConnection()
 * - try-with-resources pa fetxa konexon otomatikamenti
 */
public class E01_Connection {

    public static void main(String[] args) {
        System.out.println("=== E01: Konexon ku Bazi di Dadus ===\n");

        // try-with-resources garante ki konexon fetxa otomatikamenti
        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL,
                DatabaseConfig.USER,
                DatabaseConfig.PASSWORD)) {

            System.out.println("Konexon stabelecidu ku susesu!");
            System.out.println("URL: " + DatabaseConfig.URL);
            System.out.println("User: " + DatabaseConfig.USER);

        } catch (SQLException e) {
            System.err.println("Éru di konexon: " + e.getMessage());
            System.err.println("\nVerifika:");
            System.err.println("  1. PostgreSQL ta kori? (docker compose up -d)");
            System.err.println("  2. Porta 5432 ta disponivel?");
            System.err.println("  3. Kredensial ta koreta?");
        }
    }
}
