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

        // TODO: Kria konexon uzandu try-with-resources
        // Dika: Uza DriverManager.getConnection(URL, USER, PASSWORD)
        // Lenbra: try-with-resources garante ki konexon ta fetxa otomatikamenti!

        // try (Connection conn = DriverManager.getConnection(
        //         DatabaseConfig.URL,
        //         DatabaseConfig.USER,
        //         DatabaseConfig.PASSWORD)) {
        //
        //     System.out.println("Konexon stabelecidu ku susesu!");
        //
        // } catch (SQLException e) {
        //     System.err.println("Éru di konexon: " + e.getMessage());
        // }
    }
}
