package com.example.examples;

import com.example.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Ezénplu 02: Inseri dadus (CREATE)
 *
 * Demonstra:
 * - PreparedStatement ku placeholder (?)
 * - setString() pa defini valoris
 * - executeUpdate() pa INSERT/UPDATE/DELETE
 */
public class E02_InsertUser {

    public static void main(String[] args) {
        System.out.println("=== E02: Inseri User (INSERT) ===\n");

        // SQL ku placeholder (?) pa seguransa kontra SQL injection
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL,
                DatabaseConfig.USER,
                DatabaseConfig.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Defini valoris pa kada placeholder
            stmt.setString(1, "Ana Costa");
            stmt.setString(2, "ana@skola.dev");

            // executeUpdate() retorna númeru di linhas afetadu
            int rowsAffected = stmt.executeUpdate();

            System.out.println("Inseri " + rowsAffected + " linha!");
            System.out.println("User novu: Ana Costa (ana@skola.dev)");

        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate key")) {
                System.err.println("Éru: Email ja izisti na bazi di dadus!");
            } else {
                System.err.println("Éru: " + e.getMessage());
            }
        }
    }
}
