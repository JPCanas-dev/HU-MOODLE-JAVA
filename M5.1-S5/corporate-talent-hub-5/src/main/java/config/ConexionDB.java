package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    
    // Datos para conectarse a PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5436/corporateTalentHub";
    private static final String USER = "corporateTalentHub";
    private static final String PASSWORD = "12345";

    // Crea y retorna la conexión con la base de datos
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
}
