package com.corporatetalenthub;

import com.corporatetalenthub.modelo.Empleado;

// Sirve para capturar errores como: (ingrese la edad: hola)
import java.util.InputMismatchException;

// Sirve para leer datos del teclado, es pomo el input en python
import java.util.Scanner;

import java.util.ArrayList;
import java.util.HashMap;

public class App {

    // Final: este atributo solo podrá recibir un valor una sola vez.
    private static final int MAXIMO_EMPLEADOS = 50;
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

            // Se crean arreglos y matriz con su cantidad de posiciones con valor null
            var empleados = new ArrayList<Empleado>();
            var empleadosPorId = new HashMap<String, Empleado>();
            var calificaciones = new double[MAXIMO_EMPLEADOS][CANTIDAD_TRIMESTRES];
            var cantidadEmpleados = 0; // (var = int) porque 0 es int
            var sistemaActivo = true; // (var = boolean) porque true es boolean

            do {
                mostrarMenu();

                try {
                    System.err.println("Seleccione una opción: ");
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
                            
                            if (cantidadEmpleados >= MAXIMO_EMPLEADOS) {
                                System.out.println("No hay espacios para más empleados");
                            } else {
                                var registrado = registrarEmpleado(
                                        scanner,
                                        empleados,
                                        calificaciones);
                            }
                            break;

                        case 2:
                            mostrarReporte(empleados, calificaciones);
                            break;

                        case 3:
                            mostrarCategoriasSalariales();
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
                0. Salir
                """);
    }

    private static boolean registrarEmpleado(
            Scanner scanner,
            ArrayList<Empleado> empleados,
            double[][] calificaciones) 
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
        
        var posicion = empleados.size();
        
        for (var trimestre = 0; trimestre < CANTIDAD_TRIMESTRES; trimestre++) {
            
            System.out.printf(
                    "Calificación del trimestre %d (0 a 100): ",
                    trimestre + 1);
            var calificacion = scanner.nextDouble();

            if (calificacion < NOTA_MINIMA || calificacion > NOTA_MAXIMA) {
                System.out.println("La calificación está fuera del rango permitido.");
                scanner.nextLine();
                return false;
            }

            calificaciones[posicion][trimestre] = calificacion;
        }

        scanner.nextLine();
        var empleado = new Empleado(id, nombre, edad, salario);
        empleados.add(empleado);
        empleadosPorId.put(String.valueOf(id), empleado);
        System.out.println("Empleado registrado correctamente.");
        return true;
    }

    private static boolean idRepetido(
            ArrayList<Empleado> empleados,
            int idBuscado) {
        
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
            double[][] calificaciones) {

        if (empleados.isEmpty()) {
            System.out.println("Todavía no hay empleados registrados.");
            return;
        }

        System.out.println("\nREPORTE DE DESEMPEÑO");

        for (var fila = 0; fila < empleados.size(); fila++) {
            var suma = 0.0;

            // Los dos for forman el recorrido anidado de la matriz.
            for (var columna = 0;
                    columna < CANTIDAD_TRIMESTRES;
                    columna++) {
                suma += calificaciones[fila][columna];
            }

            var promedio = suma / CANTIDAD_TRIMESTRES;
            empleados[fila].setPromedioDesempenio(promedio);

            /*
             * Casting explícito de double a int. Se elimina la parte decimal, no
             * se redondea: 89.99 se convierte en 89. Esto implica pérdida de precisión.
             */
            var puntajeSimplificado = (int) promedio;

            // Operador ternario: condición ? resultadoSiTrue : resultadoSiFalse.
            var estadoPromocion = promedio >= PROMEDIO_DESEMPENIO
                    ? "PROMOVIDO"
                    : "NO PROMOVIDO";

            var categoria = obtenerCategoriaSalarial(
                    empleados[fila].getSalario());

            System.out.printf(
                    "ID: %d | Nombre: %s | Promedio: %.2f | "
                    + "Simplificado: %d | Estado: %s | Categoría: %s%n",
                    empleados[fila].getId(),
                    empleados[fila].getNombre(),
                    promedio,
                    puntajeSimplificado,
                    estadoPromocion,
                    categoria);
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
            int idBuscado) {

        var empleado = empleadosPorId.remove(String.valueOf(idBuscado));

        if (empleado != null) {
            empleados.remove(empleado);
            return true;
        }

        return false;
    }

}