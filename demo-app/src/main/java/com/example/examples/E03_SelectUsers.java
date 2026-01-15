package com.example.examples;

import com.example.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Ezénplu 03: Le dadus (READ)
 *
 * Demonstra:
 * - executeQuery() pa SELECT
 * - ResultSet komu kursor
 * - Iterason ku while (rs.next())
 * - getLong(), getString() pa extrai dadus
 */
public class E03_SelectUsers {

    public static void main(String[] args) {
        System.out.println("=== E03: Lista Users (SELECT) ===\n");

        String sql = "SELECT id, name, email FROM users ORDER BY id";

        try (Connection conn = DriverManager.getConnection(
                DatabaseConfig.URL,
                DatabaseConfig.USER,
                DatabaseConfig.PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("ID  | Nomi                 | Email");
            System.out.println("----|----------------------|------------------------");

            // ResultSet é kursor - rs.next() move pa prósima linha
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                System.out.printf("% -3d | %-20s | %s%n", id, name, email);
            }

        } catch (SQLException e) {
            System.err.println("Éru: " + e.getMessage());
        }
    }
}
