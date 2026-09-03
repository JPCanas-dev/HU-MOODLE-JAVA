package com.corporatetalenthub.dao;

import com.corporatetalenthub.modelo.Empleado;
import java.util.List;

public interface EmpleadoDAO {

    void insertar(Empleado empleado);

    List<Empleado> listar();

    void actualizar(Empleado empleado);

    void eliminar(int id);
}