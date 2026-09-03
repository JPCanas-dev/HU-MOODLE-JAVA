package com.corporatetalenthub.dao;

import java.util.List;

public interface CalificacionDAO {
    void insertar(int empleadoId, int trimestre, double calificacion);
    List<double[]> listarPorEmpleado(int empleadoId);
    void eliminarPorEmpleado(int empleadoId);
}