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

        System.out.println("ID  | Nomi                 | Email");
        System.out.println("----|----------------------|------------------------");

        // TODO: Implementa SELECT
        // 1. Kria Connection, PreparedStatement (try-with-resources)
        // 2. Xama stmt.executeQuery() pa otén ResultSet
        // 3. Loop ku while (rs.next()) { ... }
        // 4. Uza rs.getLong("id"), rs.getString("name"), etc.
    }
}
