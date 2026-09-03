package com.corporatetalenthub.dao;

import com.corporatetalenthub.modelo.Desarrollador;
import com.corporatetalenthub.modelo.Empleado;
import com.corporatetalenthub.modelo.Gerente;
import config.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAOImpl implements EmpleadoDAO {

    private static final String INSERT_SQL = """
        INSERT INTO empleado
        (id, nombre, edad, salario, tipo, lenguaje_principal, presupuesto_mensual, promedio_desempenio)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

    private static final String SELECT_ALL_SQL = """
        SELECT id, nombre, edad, salario, tipo,
               lenguaje_principal, presupuesto_mensual, promedio_desempenio
        FROM empleado
        ORDER BY id
        """;

    private static final String UPDATE_SQL = """
        UPDATE empleado
        SET nombre = ?, edad = ?, salario = ?,
            lenguaje_principal = ?, presupuesto_mensual = ?,
            promedio_desempenio = ?
        WHERE id = ?
        """;

    private static final String DELETE_SQL =
        "DELETE FROM empleado WHERE id = ?";

    @Override
    public void insertar(Empleado empleado) {
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {

            statement.setInt(1, empleado.getId());
            statement.setString(2, empleado.getNombre());
            statement.setByte(3, empleado.getEdad());
            statement.setDouble(4, empleado.getSalario());

            if (empleado instanceof Desarrollador desarrollador) {
                statement.setString(5, "DESARROLLADOR");
                statement.setString(6, desarrollador.getLenguajePrincipal());
                statement.setNull(7, Types.NUMERIC);
            } else if (empleado instanceof Gerente gerente) {
                statement.setString(5, "GERENTE");
                statement.setNull(6, Types.VARCHAR);
                statement.setDouble(7, gerente.getPresupuestoMensual());
            }

            statement.setDouble(8, empleado.getPromedioDesempenio());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar empleado", e);
        }
    }

    @Override
    public List<Empleado> listar() {
        List<Empleado> empleados = new ArrayList<>();

        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                empleados.add(mapearEmpleado(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar empleados", e);
        }

        return empleados;
    }

    @Override
    public void actualizar(Empleado empleado) {
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, empleado.getNombre());
            statement.setByte(2, empleado.getEdad());
            statement.setDouble(3, empleado.getSalario());

            if (empleado instanceof Desarrollador desarrollador) {
                statement.setString(4, desarrollador.getLenguajePrincipal());
                statement.setNull(5, Types.NUMERIC);
            } else if (empleado instanceof Gerente gerente) {
                statement.setNull(4, Types.VARCHAR);
                statement.setDouble(5, gerente.getPresupuestoMensual());
            }

            statement.setDouble(6, empleado.getPromedioDesempenio());
            statement.setInt(7, empleado.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar empleado", e);
        }
    }

    @Override
    public void eliminar(int id) {
        try (Connection connection = ConexionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar empleado", e);
        }
    }

    private Empleado mapearEmpleado(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String nombre = resultSet.getString("nombre");
        byte edad = resultSet.getByte("edad");
        double salario = resultSet.getDouble("salario");
        String tipo = resultSet.getString("tipo");
        double promedio = resultSet.getDouble("promedio_desempenio");

        Empleado empleado;

        if ("DESARROLLADOR".equals(tipo)) {
            empleado = new Desarrollador(
                id,
                nombre,
                edad,
                salario,
                resultSet.getString("lenguaje_principal")
            );
        } else {
            empleado = new Gerente(
                id,
                nombre,
                edad,
                salario,
                resultSet.getDouble("presupuesto_mensual")
            );
        }

        empleado.setPromedioDesempenio(promedio);
        return empleado;
    }
}