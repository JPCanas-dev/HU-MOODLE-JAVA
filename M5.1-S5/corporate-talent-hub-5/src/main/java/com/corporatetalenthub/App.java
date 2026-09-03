package com.corporatetalenthub;

import com.corporatetalenthub.controller.EmpleadoController;
import com.corporatetalenthub.dao.CalificacionDAO;
import com.corporatetalenthub.dao.CalificacionDAOImpl;
import com.corporatetalenthub.dao.EmpleadoDAO;
import com.corporatetalenthub.dao.EmpleadoDAOImpl;
import com.corporatetalenthub.view.EmpleadoView;

public class App {
    public static void main(String[] args) {
        EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();
        CalificacionDAO calificacionDAO = new CalificacionDAOImpl();
        EmpleadoController controller = new EmpleadoController(empleadoDAO, calificacionDAO);
        EmpleadoView view = new EmpleadoView(controller);
        view.iniciar();
    }
}