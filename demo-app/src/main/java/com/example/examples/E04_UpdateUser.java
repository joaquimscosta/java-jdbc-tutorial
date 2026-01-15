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

        // TODO: Implementa UPDATE
        // 1. Kria Connection i PreparedStatement (try-with-resources)
        // 2. Defini valoris pa kada placeholder
        //    - stmt.setString(1, "maria.silva@skola.dev")  // novu email
        //    - stmt.setString(2, "Maria Silva")           // nomi di user
        // 3. Xama executeUpdate() i verifika linhas afetadu
    }
}
