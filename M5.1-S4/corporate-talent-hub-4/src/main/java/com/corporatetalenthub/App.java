package com.corporatetalenthub;

import com.corporatetalenthub.modelo.ConsultorExterno;
import com.corporatetalenthub.modelo.Desarrollador;
import com.corporatetalenthub.modelo.DesempenioReport;
import com.corporatetalenthub.modelo.Empleado;
import com.corporatetalenthub.modelo.Gerente;
import com.corporatetalenthub.modelo.Persona;
import com.corporatetalenthub.modelo.Promocionable;

import java.util.InputMismatchException;

import java.util.Scanner;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

public class App {

    private static final int CANTIDAD_TRIMESTRES = 3;
    private static final double NOTA_MINIMA = 0.0;
    private static final double NOTA_MAXIMA = 100.0;
    private static final double PROMEDIO_DESEMPENIO = 80.0;

    public static void main(String[] args) {

        try (var scanner = new Scanner(System.in)) {

            var empleados = new ArrayList<Empleado>();
            var empleadosPorId = new HashMap<String, Empleado>();

            var tecnologias = List.of("Java", "JavaScript", "Python", "SQL");
            var sedes = Map.of(
                    "Barranquilla", "Colombia",
                    "Bogotá", "Colombia",
                    "Medellín", "Colombia"
            );
            var calificaciones = new ArrayList<double[]>();
            var sistemaActivo = true;

            do {
                mostrarMenu();

                try {
                    System.out.println("Seleccione una opción: ");
                    var opcion = scanner.nextInt();
                    scanner.nextLine();

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
                            System.out.println("Empleados restantes: " + empleados.size());
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

                        case 7:

                            System.out.print("ID del empleado para calcular bono de ascenso: ");
                            var idBono = scanner.nextInt();
                            calcularBonoAscenso(empleadosPorId, idBono);
                            break;

                        case 8:

                            System.out.print("ID del empleado para comparar validación Legacy vs Moderna: ");
                            var idDemo = scanner.nextInt();
                            compararValidacionLegacyVsModerna(empleadosPorId, idDemo);
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

                    scanner.nextLine();

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
                7. Calcular bono de ascenso (Promocionable)
                8. Demo: validación Legacy vs Pattern Matching
                0. Salir
                """);
    }

    private static boolean registrarEmpleado(
            Scanner scanner,
            ArrayList<Empleado> empleados,
            HashMap<String, Empleado> empleadosPorId,
            ArrayList<double[]> calificaciones) {

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

        var edad = (byte) edadIngresada;

        System.out.print("Salario mayor que cero: ");
        var salario = scanner.nextDouble();

        if (salario <= 0) {
            System.out.println("El salario debe ser mayor que cero.");
            scanner.nextLine();
            return false;
        }

        // TASK 3: el rol determina la subclase concreta que se instancia.
        // Empleado es sealed y abstracta: ya no se puede hacer "new Empleado(...)"
        // directamente, lo que obliga a modelar siempre un rol explícito.
        System.out.println("Tipo de empleado -> 1: Desarrollador | 2: Gerente");
        System.out.print("Seleccione el tipo: ");
        var tipo = scanner.nextInt();
        scanner.nextLine();

        Empleado empleado;

        if (tipo == 1) {
            System.out.print("Lenguaje principal: ");
            var lenguaje = scanner.nextLine().trim();
            empleado = new Desarrollador(id, nombre, edad, salario, lenguaje);
        } else if (tipo == 2) {
            System.out.print("Presupuesto mensual que gestiona: ");
            var presupuesto = scanner.nextDouble();
            scanner.nextLine();
            empleado = new Gerente(id, nombre, edad, salario, presupuesto);
        } else {
            System.out.println("Tipo de empleado no válido.");
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
            HashMap<String, Empleado> empleadosPorId,
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

        // TASK 2: cada fila del reporte de fin de mes se emite como un
        // DesempenioReport inmutable (record), en vez de imprimir campos sueltos.
        for (var fila = 0; fila < empleados.size(); fila++) {
            var suma = 0.0;

            for (var columna = 0; columna < CANTIDAD_TRIMESTRES; columna++) {
                suma += calificaciones.get(fila)[columna];
            }

            var promedio = suma / CANTIDAD_TRIMESTRES;
            empleados.get(fila).setPromedioDesempenio(promedio);

            var puntajeSimplificado = (int) promedio;

            var estadoPromocion = promedio >= PROMEDIO_DESEMPENIO
                    ? "PROMOVIDO"
                    : "NO PROMOVIDO";

            var categoria = obtenerCategoriaSalarial(empleados.get(fila).getSalario());

            var reporte = DesempenioReport.de(empleados.get(fila)); // record inmutable de fin de mes

            System.out.printf(
                    "ID: %d | Nombre: %s | Promedio: %.2f | "
                    + "Simplificado: %d | Estado: %s | Categoría: %s | Feedback: %s%n",
                    empleados.get(fila).getId(),
                    empleados.get(fila).getNombre(),
                    promedio,
                    puntajeSimplificado,
                    estadoPromocion,
                    categoria,
                    reporte.feedback()); // accesor autogenerado por el record
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

    // TASK 4: usa la interfaz Promocionable (método abstracto + default) sin
    // importar si el empleado es Desarrollador o Gerente: polimorfismo puro.
    private static void calcularBonoAscenso(HashMap<String, Empleado> empleadosPorId, int idBuscado) {

        var empleado = empleadosPorId.get(String.valueOf(idBuscado));

        if (empleado == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }

        // Pattern Matching for instanceof: solo entra al bloque si además
        // implementa Promocionable, y ya queda con el tipo correcto.
        if (empleado instanceof Promocionable promocionable) {
            promocionable.registrarLog(empleado.getNombre()); // método default heredado
        } else {
            System.out.println("Este empleado no es promocionable.");
        }
    }

    /**
     * TASK 3: comparación directa Legacy vs Moderno para el mismo problema:
     * "acceder a un atributo específico de una subclase de Empleado".
     */
    private static void compararValidacionLegacyVsModerna(
            HashMap<String, Empleado> empleadosPorId, int idBuscado) {

        var empleado = empleadosPorId.get(String.valueOf(idBuscado));

        if (empleado == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }

        System.out.println("--- Estilo Legacy (Java 8/11): instanceof + casting manual ---");
        if (empleado instanceof Desarrollador) {
            // Casting manual obligatorio, propenso a ClassCastException si se
            // olvida el chequeo previo, y hay que repetir el cast por cada uso.
            var lenguaje = ((Desarrollador) empleado).getLenguajePrincipal();
            System.out.println("Lenguaje principal: " + lenguaje);
        } else if (empleado instanceof Gerente) {
            var presupuesto = ((Gerente) empleado).getPresupuestoMensual();
            System.out.println("Presupuesto mensual: " + presupuesto);
        }

        System.out.println("--- Estilo Moderno (Java 17/21): Pattern Matching for instanceof ---");
        // El compilador "extrae" la variable ya casteada (des / ger) dentro
        // de cada rama: sin cast manual, sin riesgo de ClassCastException,
        // y con acceso directo a los métodos propios de cada subclase.
        if (empleado instanceof Desarrollador des) {
            System.out.println("Lenguaje principal: " + des.getLenguajePrincipal());
        } else if (empleado instanceof Gerente ger) {
            System.out.println("Presupuesto mensual: " + ger.getPresupuestoMensual());
        }

        System.out.println("--- Bonus: switch pattern matching exhaustivo sobre Persona (sealed) ---");
        System.out.println(describirPersona(empleado));
    }

    // Como Persona es sealed y solo permite Empleado/ConsultorExterno (y
    // Empleado a su vez solo permite Desarrollador/Gerente), el compilador
    // sabe que este switch es EXHAUSTIVO: no hace falta "default", y si
    // màs adelante se agrega un nuevo subtipo, este método dejará de compilar
    // hasta que se cubra el caso nuevo, evitando bugs por olvido.
    private static String describirPersona(Persona persona) {
        return switch (persona) {
            case Desarrollador des -> "Desarrollador de " + des.getLenguajePrincipal();
            case Gerente ger -> "Gerente con presupuesto " + ger.getPresupuestoMensual();
            case ConsultorExterno con -> "Consultor de " + con.getEmpresaContratante();
        };
    }

}