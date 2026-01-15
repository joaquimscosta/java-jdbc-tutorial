package com.example.examples;

import com.example.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Ezénplu 05: Apaga dadus (DELETE)
 *
 * Demonstra:
 * - DELETE ku WHERE kláuzula
 * - Importánsia di WHERE pa evita apaga tudu dadus
 */
public class E05_DeleteUser {

    public static void main(String[] args) {
        System.out.println("=== E05: Apaga User (DELETE) ===\n");

        // KUIDADU: Sen WHERE, es query ta apaga TUDU user!
        // DELETE FROM users;  <-- PERIGOZU!
        String sql = "DELETE FROM users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL,
                DatabaseConfig.USER,
                DatabaseConfig.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "ana@skola.dev");

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Apaga " + rowsAffected + " linha!");
                System.out.println("User ana@skola.dev foi apagadu.");
            } else {
                System.out.println("Ninhun user inkontradu ku es email.");
            }

        } catch (SQLException e) {
            System.err.println("Éru: " + e.getMessage());
        }
    }
}
