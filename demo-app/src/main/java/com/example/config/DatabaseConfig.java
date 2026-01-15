package com.example.config;

/**
 * Konfigurason di bazi di dadus pa tutorial JDBC.
 * Valoris ta korrespondi ku docker-compose.yml
 */
public final class DatabaseConfig {

    // URL di konexon: jdbc:postgresql://host:porta/nomi_bazi
    public static final String URL = "jdbc:postgresql://localhost:5432/skola_dev";

    // Kredensial
    public static final String USER = "skola";
    public static final String PASSWORD = "skola_dev";

    private DatabaseConfig() {
        // Klasi utilitáriu - ka instansia
    }
}
