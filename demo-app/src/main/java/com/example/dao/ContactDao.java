package com.example.dao;

import com.example.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) pa tabela contacts.
 *
 * DAO pattern organiza tudu operason di bazi di dadus
 * num lugar sentral, separandu lójika di dadus di lójika di aplikason.
 */
public class ContactDao {

    // Reprezentason di un kontaktu
    public record Contact(long id, String name, String phone, String email) {}

    /**
     * Kria kontaktu novu na bazi di dadus.
     *
     * @return ID di kontaktu novu kriadu
     */
    public long create(String name, String phone, String email) throws SQLException {
        String sql = "INSERT INTO contacts (name, phone, email) VALUES (?, ?, ?) RETURNING id";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
                throw new SQLException("Falha na kriason di kontaktu");
            }
        }
    }

    /**
     * Buska tudu kontaktus.
     *
     * @return Lista di tudu kontaktus
     */
    public List<Contact> findAll() throws SQLException {
        String sql = "SELECT id, name, phone, email FROM contacts ORDER BY name";
        List<Contact> contacts = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email")
                ));
            }
        }
        return contacts;
    }

    /**
     * Buska kontaktu pa ID.
     *
     * @return Kontaktu ou null si ka inkontra
     */
    public Contact findById(long id) throws SQLException {
        String sql = "SELECT id, name, phone, email FROM contacts WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Contact(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("email")
                    );
                }
                return null;
            }
        }
    }

    /**
     * Atualiza kontaktu izistenti.
     *
     * @return true si atualizason foi susesu
     */
    public boolean update(long id, String name, String phone, String email) throws SQLException {
        String sql = "UPDATE contacts SET name = ?, phone = ?, email = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.setLong(4, id);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Apaga kontaktu pa ID.
     *
     * @return true si kontaktu foi apagadu
     */
    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM contacts WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Kria konexon ku bazi di dadus.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.URL,
                DatabaseConfig.USER,
                DatabaseConfig.PASSWORD
        );
    }
}
