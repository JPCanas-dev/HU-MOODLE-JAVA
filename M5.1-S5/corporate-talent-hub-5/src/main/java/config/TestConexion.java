package config;

import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        try (Connection connection = ConexionDB.getConnection()) {
            System.out.println("Conexión exitosa a PostgreSQL.");
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}