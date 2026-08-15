package com.corporatetalenthub;

import com.corporatetalenthub.modelo.Empleado;

// Sirve para capturar errores como: (ingrese la edad: hola)
import java.util.InputMismatchException;

// Sirve para leer datos del teclado, es pomo el input en python
import java.util.Scanner;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

public class App {

    // Final: este atributo solo podrá recibir un valor una sola vez.
    private static final int CANTIDAD_TRIMESTRES = 3;
    private static final double NOTA_MINIMA = 0.0;
    private static final double NOTA_MAXIMA = 100.0;
    private static final double PROMEDIO_DESEMPENIO = 80.0;

    public static void main(String[] args) {

        // Cuando se usa un recurso, como System.in, es decir, la entrada del teclado, es buena 
        // práctica cerrarlos como con scanner.close().
        
        // Aquí aparece el try-with-resources en try (Scanner scanner = new Scanner(System.in)) {
        // Cuando el programa salga del bloque de ese try, java hace el close automáticamente.
        // Aquí el try sirve para cerrar recursos automáticamente, no para try-catch
        
        // var es para no repetir el tipo, en lugar de Scanner scanner, se escribe var scanner. El
        // El compilador sabe que new Scanner(System.in) produce un objeto.
        
        try (var scanner = new Scanner(System.in)) {

            // Se crean las colecciones para almacenar los datos.
            var empleados = new ArrayList<Empleado>();
            var empleadosPorId = new HashMap<String, Empleado>();
            
            /*
            * List.of() y Map.of() crean colecciones inmutables.
            * Esto evita modificaciones accidentales en datos de configuración.
            * A diferencia de ArrayList y HashMap, no permiten add(), put() o remove().
             */
            var tecnologias = List.of("Java", "JavaScript", "Python", "SQL");
            var sedes = Map.of(
                    "Barranquilla", "Colombia",
                    "Bogotá", "Colombia",
                    "Medellín", "Colombia"
            );
            var calificaciones = new ArrayList<double[]>();
            var sistemaActivo = true; // (var = boolean) porque true es boolean

            do {
                mostrarMenu();

                try {
                    System.out.println("Seleccione una opción: ");
                    var opcion = scanner.nextInt();
                    scanner.nextLine(); // Consume el salto de línea pendiente.

                    /*
                     * Switch tradicional, compatible con Java 8. Cada case necesita break para impedir  
                     * el fall-through. Si se olvida, Java continúa ejecutando el siguiente case. La 
                     * Switch Expression moderna con -> no tiene ese riesgo por defecto y, además puede
                     * producir directamente un valor.
                     */
                    
                    switch (opcion) {

                        case 1:
                            
                            registrarEmpleado(
                                    scanner,
                                    empleados,
                                    empleadosPorId,
                                    calificaciones);
                            break;

                        case 2:
                            mostrarReporte(empleados, calificaciones);
                            break;

                        case 3:
                            mostrarCategoriasSalariales();
                            break;
                            
                        case 4:

                            System.out.print("ID del empleado a eliminar: ");
                            var idEliminar = scanner.nextInt();

                            if (eliminarEmpleado(empleados, empleadosPorId, calificaciones, idEliminar)) {
                                System.out.println("Empleado eliminado correctamente.");
                            } else {
                                System.out.println("Empleado no encontrado.");
                            }
                            break;

                        case 5:

                            var cantidadAntes = empleados.size();
                            filtrarEmpleados(empleados, empleadosPorId, calificaciones);
                            var cantidadEliminados = cantidadAntes - empleados.size();
                            System.out.println("Empleados eliminados por bajo desempeño: " + cantidadEliminados);
                            System.out.println( "Empleados restantes: " + empleados.size());
                            break;
                            
                        case 6:
                            
                            System.out.print("ID del empleado a buscar: ");
                            var idBuscar = scanner.nextInt();

                            var empleadoEncontrado = buscarEmpleadoPorId(empleadosPorId, idBuscar);

                            if (empleadoEncontrado != null) {
                                System.out.println("Empleado encontrado: " + empleadoEncontrado.getNombre());
                            } else {
                                System.out.println("Empleado no encontrado.");
                            }
                            break;

                        case 0:
                            sistemaActivo = false;
                            System.out.println("Sesión finalizada.");
                            break;

                        default:
                            System.out.println("Opción fuera del menú");
                            break;
                    }

                } catch (InputMismatchException exception) {

                    System.out.println("Entrada inválida. Debe escribir un valor numérico del "
                            + "tipo solicitado");

                    // Descarta la entrada que provocó la excepción. Sin esta línea, Scanner intentaría 
                    // leer el mismo dato inválido nuevamente, entrando en un "catch infinito"
                    scanner.nextLine();

                    /*
                     * Java 8 ya entrega el tipo de excepción y el stack trace. Las versiones modernas 
                     * mejoraron especialmente algunos diagnósticos, como Helpful NullPointerExceptions 
                     * desde Java 14, indicando qué referencia era null en una expresión. Esto no significa 
                     * que el mensaje de toda InputMismatchException sea siempre más detallado; por eso la
                     * aplicación muestra un mensaje comprensible al usuario.
                     */
                }

            } while (sistemaActivo);

        }

    }

    private static void mostrarMenu() {
        System.out.println("""

                =====================================
                     CORPORATE TALENT HUB
                =====================================
                1. Registrar empleado y calificaciones
                2. Mostrar reporte de desempeño
                3. Consultar categorías salariales
                4. Eliminar empleado
                5. Eliminar empleados con bajo desempeño
                6. Buscar empleado por ID
                0. Salir
                """);
    }

    private static boolean registrarEmpleado(
            Scanner scanner,
            ArrayList<Empleado> empleados,
            HashMap<String, Empleado> empleadosPorId,
            ArrayList<double[]> calificaciones) 
    {

        System.out.print("ID positivo: ");
        var id = scanner.nextInt();
        scanner.nextLine();

        if (id <= 0) {
            System.out.println("El ID debe ser mayor que cero.");
            return false;
        } else if (idRepetido(empleados, id)) {
            System.out.println("Ya existe un empleado con ese ID.");
            return false;
        }

        System.out.print("Nombre: ");
        var nombre = scanner.nextLine().trim();

        if (nombre.isBlank()) {
            System.out.println("El nombre no puede estar vacío.");
            return false;
        }

        System.out.print("Edad entre 18 y 100: ");
        var edadIngresada = scanner.nextInt();

        if (edadIngresada < 18 || edadIngresada > 100) {
            System.out.println("La edad está fuera del rango permitido.");
            scanner.nextLine();
            return false;
        }

        // Scanner entrega un int; después de validar el rango se convierte a byte.
        var edad = (byte) edadIngresada;

        System.out.print("Salario mayor que cero: ");
        var salario = scanner.nextDouble();

        if (salario <= 0) {
            System.out.println("El salario debe ser mayor que cero.");
            scanner.nextLine();
            return false;
        }
        
        var calificacionEmpleado = new double[CANTIDAD_TRIMESTRES];
        
        for (var trimestre = 0; trimestre < CANTIDAD_TRIMESTRES; trimestre++) {
            
            System.out.printf("Calificación del trimestre %d (0 a 100): ", trimestre + 1);
            var calificacion = scanner.nextDouble();

            if (calificacion < NOTA_MINIMA || calificacion > NOTA_MAXIMA) {
                System.out.println("La calificación está fuera del rango permitido.");
                scanner.nextLine();
                return false;
            }
            
            calificacionEmpleado[trimestre] = calificacion;

        }
        
        calificaciones.add(calificacionEmpleado);

        scanner.nextLine();
        var empleado = new Empleado(id, nombre, edad, salario);
        empleados.add(empleado);
        empleadosPorId.put(String.valueOf(id), empleado);
        System.out.println("Empleado registrado correctamente.");
        return true;
    }

    private static boolean idRepetido(ArrayList<Empleado> empleados, int idBuscado) {
        
        for (var empleado : empleados) {
            if (empleado.getId() == idBuscado) {
                return true;
            }
        }
        return false;
    }
   
    private static Empleado buscarEmpleadoPorId(
            HashMap<String, 
            Empleado> empleadosPorId, 
            int idBuscado) {

        return empleadosPorId.get(String.valueOf(idBuscado));
    }

    private static void mostrarReporte(
            ArrayList<Empleado> empleados,
            ArrayList<double[]> calificaciones) {
        
        if (empleados.isEmpty()) {
            System.out.println("Todavía no hay empleados registrados.");
            return;
        }

        /*
        * En Java 8/11 se usaban índices manuales:
        * get(0) para el primero y get(size() - 1) para el último.
        *
        * Java 21 agrega getFirst(), getLast() y reversed(),
        * mejorando la legibilidad y reduciendo errores de índices.
         */
        var primerEmpleado = empleados.getFirst();
        var ultimoEmpleado = empleados.getLast();

        System.out.println("\nREPORTE DE DESEMPEÑO");

        System.out.println("Primer empleado: " + primerEmpleado.getNombre());
        System.out.println("Último empleado: " + ultimoEmpleado.getNombre());
        
        var empleadosReversa = empleados.reversed();
        
        System.out.println("Empleados en orden inverso:");

        for (var empleado : empleadosReversa) {
            System.out.println(empleado.getNombre());
        }

        for (var fila = 0; fila < empleados.size(); fila++) {
            var suma = 0.0;

            // Los dos for forman el recorrido anidado de la matriz.
            for (var columna = 0; columna < CANTIDAD_TRIMESTRES; columna++) {
                suma += calificaciones.get(fila)[columna];
            }

            var promedio = suma / CANTIDAD_TRIMESTRES;
            empleados.get(fila).setPromedioDesempenio(promedio);

            /*
             * Casting explícito de double a int. Se elimina la parte decimal, no
             * se redondea: 89.99 se convierte en 89. Esto implica pérdida de precisión.
             */
            var puntajeSimplificado = (int) promedio;

            // Operador ternario: condición ? resultadoSiTrue : resultadoSiFalse.
            var estadoPromocion = promedio >= PROMEDIO_DESEMPENIO
                    ? "PROMOVIDO"
                    : "NO PROMOVIDO";

            var categoria = obtenerCategoriaSalarial(empleados.get(fila).getSalario());

            System.out.printf(
                    "ID: %d | Nombre: %s | Promedio: %.2f | "
                    + "Simplificado: %d | Estado: %s | Categoría: %s%n",
                    empleados.get(fila).getId(),
                    empleados.get(fila).getNombre(),
                    promedio,
                    puntajeSimplificado,
                    estadoPromocion,
                    categoria);
        }

        var sumaSalarios = 0.0;

        for (var empleado : empleados) {
            sumaSalarios += empleado.getSalario();
        }

        var promedioSalarios = empleados.isEmpty() ? 0 : sumaSalarios / empleados.size();

        System.out.println("Total de empleados: " + empleados.size());
        System.out.println("Promedio de salarios: " + promedioSalarios);

    }

    private static void filtrarEmpleados(
            ArrayList<Empleado> empleados,
            HashMap<String, Empleado> empleadosPorId,
            ArrayList<double[]> calificaciones) {

        for (var posicion = empleados.size() - 1; posicion >= 0; posicion--) {

            var empleado = empleados.get(posicion);
            var calificacionEmpleado = calificaciones.get(posicion);

            var suma = 0.0;

            for (var calificacion : calificacionEmpleado) {
                suma += calificacion;
            }

            var promedio = suma / CANTIDAD_TRIMESTRES;
            empleado.setPromedioDesempenio(promedio);

            if (promedio < PROMEDIO_DESEMPENIO) {

                empleados.removeIf(e -> e == empleado);
                empleadosPorId.remove(String.valueOf(empleado.getId()));
                calificaciones.remove(posicion);
            }
        }
    }

    public static String obtenerCategoriaSalarial(double salario) {
        var rango = determinarRangoSalarial(salario);

        /*
         * Switch Expression moderna. La flecha evita el fall-through y el switch
         * devuelve un valor, por lo que no se necesita asignar y usar break en cada case.
         */
        return switch (rango) {
            case 1 ->
                "JUNIOR";
            case 2 ->
                "SEMISENIOR";
            case 3 ->
                "SENIOR";
            case 4 ->
                "LÍDER";
            default ->
                throw new IllegalArgumentException("Rango salarial no reconocido: " + rango);
        };
    }

    private static int determinarRangoSalarial(double salario) {
        if (salario < 2_000_000.0) {
            return 1;
        } else if (salario < 4_000_000.0) {
            return 2;
        } else if (salario < 7_000_000.0) {
            return 3;
        } else {
            return 4;
        }
    }

    private static void mostrarCategoriasSalariales() {
        System.out.println("""
                Categorías:
                - Menos de $2.000.000: JUNIOR
                - Desde $2.000.000 y menos de $4.000.000: SEMISENIOR
                - Desde $4.000.000 y menos de $7.000.000: SENIOR
                - Desde $7.000.000: LÍDER
                """);
    }
    
    private static boolean eliminarEmpleado(
            ArrayList<Empleado> empleados,
            HashMap<String, Empleado> empleadosPorId,
            ArrayList<double[]> calificaciones,
            int idBuscado) {

        var empleado = empleadosPorId.remove(String.valueOf(idBuscado));

        if (empleado != null) {
            var posicion = empleados.indexOf(empleado);
            empleados.remove(empleado);
            calificaciones.remove(posicion);
            return true;
        }
        return false;
        
    }

}