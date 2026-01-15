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

        // TODO: Implementa INSERT
        // 1. Kria Connection i PreparedStatement (try-with-resources)
        // 2. Uza stmt.setString(1, "Ana Costa") pa primeru placeholder
        // 3. Uza stmt.setString(2, "ana@skola.dev") pa segundu placeholder
        // 4. Xama stmt.executeUpdate() i imprimi linhas afetadu
    }
}
