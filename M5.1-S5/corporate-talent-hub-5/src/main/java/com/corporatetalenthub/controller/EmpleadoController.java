package com.corporatetalenthub.controller;

import com.corporatetalenthub.dao.CalificacionDAO;
import com.corporatetalenthub.dao.EmpleadoDAO;
import com.corporatetalenthub.modelo.Empleado;
import java.util.List;

public class EmpleadoController {

    private final EmpleadoDAO empleadoDAO;
    private final CalificacionDAO calificacionDAO;

    public EmpleadoController(EmpleadoDAO empleadoDAO, CalificacionDAO calificacionDAO) {
        this.empleadoDAO = empleadoDAO;
        this.calificacionDAO = calificacionDAO;
    }

    public void registrar(Empleado empleado) {
        empleadoDAO.insertar(empleado);
    }

    public void registrarCalificacion(int empleadoId, int trimestre, double calificacion) {
        calificacionDAO.insertar(empleadoId, trimestre, calificacion);
    }

    public List<Empleado> listar() {
        return empleadoDAO.listar();
    }

    public List<double[]> listarCalificaciones(int empleadoId) {
        return calificacionDAO.listarPorEmpleado(empleadoId);
    }

    public void actualizar(Empleado empleado) {
        empleadoDAO.actualizar(empleado);
    }

    public void actualizarCalificaciones(int empleadoId, double[] calificaciones) {
        calificacionDAO.eliminarPorEmpleado(empleadoId);

        for (int trimestre = 0; trimestre < calificaciones.length; trimestre++) {
            calificacionDAO.insertar(empleadoId, trimestre + 1, calificaciones[trimestre]);
        }
    }

    public void eliminar(int id) {
        empleadoDAO.eliminar(id);
    }
}