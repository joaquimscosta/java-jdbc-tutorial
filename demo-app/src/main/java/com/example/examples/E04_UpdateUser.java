package com.example.examples;

import com.example.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Ezénplu 04: Atualiza dadus (UPDATE)
 *
 * Demonstra:
 * - UPDATE ku WHERE kláuzula
 * - Múltiplu placeholder na mêmu query
 */
public class E04_UpdateUser {

    public static void main(String[] args) {
        System.out.println("=== E04: Atualiza User (UPDATE) ===\n");

        // IMPORTANTE: Sénpri uza WHERE pa evita atualiza TUDU linha!
        String sql = "UPDATE users SET email = ? WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL,
                DatabaseConfig.USER,
                DatabaseConfig.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Placeholder 1: novu email
            stmt.setString(1, "maria.silva@skola.dev");
            // Placeholder 2: nomi di user pa atualiza
            stmt.setString(2, "Maria Silva");

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Atualiza " + rowsAffected + " linha!");
                System.out.println("Email novu di Maria Silva: maria.silva@skola.dev");
            } else {
                System.out.println("Ninhun user inkontradu ku es nomi.");
            }

        } catch (SQLException e) {
            System.err.println("Éru: " + e.getMessage());
        }
    }
}
