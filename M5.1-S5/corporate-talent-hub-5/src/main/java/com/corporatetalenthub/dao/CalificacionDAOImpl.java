package com.corporatetalenthub.dao;

import config.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalificacionDAOImpl implements CalificacionDAO {

    private static final String INSERT_SQL = """
        INSERT INTO calificacion (empleado_id, trimestre, calificacion)
        VALUES (?, ?, ?)
        """;

    private static final String SELECT_SQL = """
        SELECT trimestre, calificacion
        FROM calificacion
        WHERE empleado_id = ?
        ORDER BY trimestre
        """;

    private static final String DELETE_SQL =
            "DELETE FROM calificacion WHERE empleado_id = ?";

    @Override
    public void insertar(int empleadoId, int trimestre, double calificacion) {
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {

            statement.setInt(1, empleadoId);
            statement.setInt(2, trimestre);
            statement.setDouble(3, calificacion);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar calificación", e);
        }
    }

    @Override
    public List<double[]> listarPorEmpleado(int empleadoId) {
        List<double[]> calificaciones = new ArrayList<>();

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SQL)) {

            statement.setInt(1, empleadoId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    calificaciones.add(new double[]{
                        resultSet.getInt("trimestre"),
                        resultSet.getDouble("calificacion")
                    });
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar calificaciones", e);
        }

        return calificaciones;
    }

    @Override
    public void eliminarPorEmpleado(int empleadoId) {
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setInt(1, empleadoId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar calificaciones", e);
        }
    }
}