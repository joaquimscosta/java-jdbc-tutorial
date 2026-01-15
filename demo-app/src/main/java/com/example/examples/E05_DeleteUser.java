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

        // TODO: Implementa DELETE
        // 1. Kria Connection i PreparedStatement (try-with-resources)
        // 2. Defini email pa apaga: stmt.setString(1, "ana@skola.dev")
        // 3. Xama executeUpdate()
    }
}
