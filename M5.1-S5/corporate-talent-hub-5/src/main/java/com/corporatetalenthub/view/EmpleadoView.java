package com.corporatetalenthub.view;

import com.corporatetalenthub.controller.EmpleadoController;
import com.corporatetalenthub.modelo.ConsultorExterno;
import com.corporatetalenthub.modelo.Desarrollador;
import com.corporatetalenthub.modelo.DesempenioReport;
import com.corporatetalenthub.modelo.Empleado;
import com.corporatetalenthub.modelo.Gerente;
import com.corporatetalenthub.modelo.Persona;
import com.corporatetalenthub.modelo.Promocionable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EmpleadoView {

    private static final int CANTIDAD_TRIMESTRES = 3;
    private static final double NOTA_MINIMA = 0.0;
    private static final double NOTA_MAXIMA = 100.0;
    private static final double PROMEDIO_DESEMPENIO = 80.0;

    private final EmpleadoController controller;
    private final Scanner scanner;
    private final ArrayList<Empleado> empleados;
    private final HashMap<String, Empleado> empleadosPorId;
    private final ArrayList<double[]> calificaciones;

    public EmpleadoView(EmpleadoController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
        this.empleados = new ArrayList<>(controller.listar());
        this.empleadosPorId = new HashMap<>();
        this.calificaciones = new ArrayList<>();

        for (var empleado : empleados) {
            empleadosPorId.put(String.valueOf(empleado.getId()), empleado);

            var notas = new double[CANTIDAD_TRIMESTRES];

            for (var dato : controller.listarCalificaciones(empleado.getId())) {
                notas[(int) dato[0] - 1] = dato[1];
            }

            calificaciones.add(notas);
        }
    }

    public void iniciar() {
        var sistemaActivo = true;

        do {
            mostrarMenu();

            try {
                System.out.print("Seleccione una opción: ");
                var opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> registrarEmpleado();
                    case 2 -> mostrarReporte();
                    case 3 -> mostrarCategoriasSalariales();
                    case 4 -> eliminarEmpleado();
                    case 5 -> filtrarEmpleados();
                    case 6 -> buscarEmpleado();
                    case 7 -> calcularBonoAscenso();
                    case 8 -> compararValidacionLegacyVsModerna();
                    case 9 -> actualizarEmpleado();
                    case 0 -> {
                        sistemaActivo = false;
                        System.out.println("Sesión finalizada.");
                    }
                    default -> System.out.println("Opción fuera del menú.");
                }
            } catch (InputMismatchException exception) {
                System.out.println("Entrada inválida. Debe escribir un valor numérico.");
                scanner.nextLine();
            }
        } while (sistemaActivo);

        scanner.close();
    }

    private void mostrarMenu() {
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
                7. Calcular bono de ascenso
                8. Demo: validación Legacy vs Moderna
                9. Actualizar empleado y calificaciones
                0. Salir
                """);
    }

    private void registrarEmpleado() {
        System.out.print("ID positivo: ");
        var id = scanner.nextInt();
        scanner.nextLine();

        if (id <= 0 || idRepetido(id)) {
            System.out.println(id <= 0 ? "El ID debe ser mayor que cero." : "Ya existe un empleado con ese ID.");
            return;
        }

        System.out.print("Nombre: ");
        var nombre = scanner.nextLine().trim();

        if (nombre.isBlank()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }

        System.out.print("Edad entre 18 y 100: ");
        var edadIngresada = scanner.nextInt();

        if (edadIngresada < 18 || edadIngresada > 100) {
            System.out.println("La edad está fuera del rango permitido.");
            scanner.nextLine();
            return;
        }

        var edad = (byte) edadIngresada;

        System.out.print("Salario mayor que cero: ");
        var salario = scanner.nextDouble();

        if (salario <= 0) {
            System.out.println("El salario debe ser mayor que cero.");
            scanner.nextLine();
            return;
        }

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
            return;
        }

        var notas = leerCalificaciones();

        controller.registrar(empleado);

        for (var trimestre = 0; trimestre < CANTIDAD_TRIMESTRES; trimestre++) {
            controller.registrarCalificacion(id, trimestre + 1, notas[trimestre]);
        }

        empleados.add(empleado);
        empleadosPorId.put(String.valueOf(id), empleado);
        calificaciones.add(notas);

        System.out.println("Empleado registrado correctamente.");
    }

    private double[] leerCalificaciones() {
        var notas = new double[CANTIDAD_TRIMESTRES];

        for (var trimestre = 0; trimestre < CANTIDAD_TRIMESTRES; trimestre++) {
            System.out.printf("Calificación del trimestre %d (0 a 100): ", trimestre + 1);
            var nota = scanner.nextDouble();

            if (nota < NOTA_MINIMA || nota > NOTA_MAXIMA) {
                throw new IllegalArgumentException("La calificación debe estar entre 0 y 100.");
            }

            notas[trimestre] = nota;
        }

        scanner.nextLine();
        return notas;
    }

    private void actualizarEmpleado() {
        System.out.print("ID del empleado a actualizar: ");
        var id = scanner.nextInt();
        scanner.nextLine();

        var empleadoAnterior = empleadosPorId.get(String.valueOf(id));

        if (empleadoAnterior == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }

        System.out.print("Nuevo nombre: ");
        var nombre = scanner.nextLine().trim();

        System.out.print("Nueva edad: ");
        var edad = (byte) scanner.nextInt();

        System.out.print("Nuevo salario: ");
        var salario = scanner.nextDouble();
        scanner.nextLine();

        Empleado actualizado;

        if (empleadoAnterior instanceof Desarrollador desarrollador) {
            System.out.print("Nuevo lenguaje principal: ");
            var lenguaje = scanner.nextLine().trim();
            actualizado = new Desarrollador(id, nombre, edad, salario, lenguaje);
        } else {
            System.out.print("Nuevo presupuesto mensual: ");
            var presupuesto = scanner.nextDouble();
            scanner.nextLine();
            actualizado = new Gerente(id, nombre, edad, salario, presupuesto);
        }

        var notas = leerCalificaciones();

        controller.actualizar(actualizado);
        controller.actualizarCalificaciones(id, notas);

        var posicion = empleados.indexOf(empleadoAnterior);
        empleados.set(posicion, actualizado);
        empleadosPorId.put(String.valueOf(id), actualizado);
        calificaciones.set(posicion, notas);

        System.out.println("Empleado actualizado correctamente.");
    }

    private boolean idRepetido(int idBuscado) {
        for (var empleado : empleados) {
            if (empleado.getId() == idBuscado) {
                return true;
            }
        }
        return false;
    }

    private void mostrarReporte() {
        if (empleados.isEmpty()) {
            System.out.println("Todavía no hay empleados registrados.");
            return;
        }

        System.out.println("\nREPORTE DE DESEMPEÑO");

        for (var fila = 0; fila < empleados.size(); fila++) {
            var notas = calificaciones.get(fila);
            var suma = 0.0;

            for (var nota : notas) {
                suma += nota;
            }

            var promedio = suma / CANTIDAD_TRIMESTRES;
            var empleado = empleados.get(fila);

            empleado.setPromedioDesempenio(promedio);

            var estado = promedio >= PROMEDIO_DESEMPENIO ? "PROMOVIDO" : "NO PROMOVIDO";
            var categoria = obtenerCategoriaSalarial(empleado.getSalario());
            var reporte = DesempenioReport.de(empleado);

            System.out.printf(
                    "ID: %d | Nombre: %s | Promedio: %.2f | Estado: %s | Categoría: %s | Feedback: %s%n",
                    empleado.getId(),
                    empleado.getNombre(),
                    promedio,
                    estado,
                    categoria,
                    reporte.feedback()
            );
        }

        var sumaSalarios = 0.0;

        for (var empleado : empleados) {
            sumaSalarios += empleado.getSalario();
        }

        System.out.println("Total de empleados: " + empleados.size());
        System.out.println("Promedio de salarios: " + sumaSalarios / empleados.size());

        generarReporteFinal();
    }

    private void generarReporteFinal() {
        var reporte = """
                =====================================
                REPORTE CONSOLIDADO - CORPORATE TALENT HUB
                =====================================
                Total de empleados: %d
                Persistencia: PostgreSQL mediante JDBC
                Acceso a datos: DAO + PreparedStatement
                Arquitectura: MVC
                Gestión de recursos: try-with-resources
                Transferencia de datos: Java Record
                Compatibilidad moderna: Java 21
                =====================================
                """.formatted(empleados.size());

        System.out.println(reporte);
    }

    private void eliminarEmpleado() {
        System.out.print("ID del empleado a eliminar: ");
        var id = scanner.nextInt();

        var empleado = empleadosPorId.get(String.valueOf(id));

        if (empleado == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }

        controller.eliminar(id);

        var posicion = empleados.indexOf(empleado);
        empleados.remove(posicion);
        empleadosPorId.remove(String.valueOf(id));
        calificaciones.remove(posicion);

        System.out.println("Empleado eliminado correctamente.");
    }

    private void filtrarEmpleados() {
        var cantidadAntes = empleados.size();

        for (var posicion = empleados.size() - 1; posicion >= 0; posicion--) {
            var notas = calificaciones.get(posicion);
            var suma = 0.0;

            for (var nota : notas) {
                suma += nota;
            }

            var promedio = suma / CANTIDAD_TRIMESTRES;

            if (promedio < PROMEDIO_DESEMPENIO) {
                controller.eliminar(empleados.get(posicion).getId());
                empleadosPorId.remove(String.valueOf(empleados.get(posicion).getId()));
                empleados.remove(posicion);
                calificaciones.remove(posicion);
            }
        }

        System.out.println("Empleados eliminados por bajo desempeño: " + (cantidadAntes - empleados.size()));
        System.out.println("Empleados restantes: " + empleados.size());
    }

    private void buscarEmpleado() {
        System.out.print("ID del empleado a buscar: ");
        var id = scanner.nextInt();

        var empleado = empleadosPorId.get(String.valueOf(id));

        if (empleado != null) {
            System.out.println("Empleado encontrado: " + empleado.getNombre());
        } else {
            System.out.println("Empleado no encontrado.");
        }
    }

    private void calcularBonoAscenso() {
        System.out.print("ID del empleado para calcular bono de ascenso: ");
        var id = scanner.nextInt();

        var empleado = empleadosPorId.get(String.valueOf(id));

        if (empleado == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }

        if (empleado instanceof Promocionable promocionable) {
            promocionable.registrarLog(empleado.getNombre());
        } else {
            System.out.println("Este empleado no es promocionable.");
        }
    }

    private void compararValidacionLegacyVsModerna() {
        System.out.print("ID del empleado: ");
        var id = scanner.nextInt();

        var empleado = empleadosPorId.get(String.valueOf(id));

        if (empleado == null) {
            System.out.println("Empleado no encontrado.");
            return;
        }

        System.out.println("--- Legacy ---");

        if (empleado instanceof Desarrollador) {
            var des = (Desarrollador) empleado;
            System.out.println("Lenguaje: " + des.getLenguajePrincipal());
        } else if (empleado instanceof Gerente) {
            var ger = (Gerente) empleado;
            System.out.println("Presupuesto: " + ger.getPresupuestoMensual());
        }

        System.out.println("--- Java 17/21 ---");

        if (empleado instanceof Desarrollador des) {
            System.out.println("Lenguaje: " + des.getLenguajePrincipal());
        } else if (empleado instanceof Gerente ger) {
            System.out.println("Presupuesto: " + ger.getPresupuestoMensual());
        }

        System.out.println(describirPersona(empleado));
    }

    public static String obtenerCategoriaSalarial(double salario) {
        var rango = determinarRangoSalarial(salario);

        return switch (rango) {
            case 1 -> "JUNIOR";
            case 2 -> "SEMISENIOR";
            case 3 -> "SENIOR";
            case 4 -> "LÍDER";
            default -> throw new IllegalArgumentException("Rango no reconocido.");
        };
    }

    private static int determinarRangoSalarial(double salario) {
        if (salario < 2_000_000) return 1;
        if (salario < 4_000_000) return 2;
        if (salario < 7_000_000) return 3;
        return 4;
    }

    private void mostrarCategoriasSalariales() {
        System.out.println("""
                Categorías:
                - Menos de $2.000.000: JUNIOR
                - $2.000.000 a menos de $4.000.000: SEMISENIOR
                - $4.000.000 a menos de $7.000.000: SENIOR
                - $7.000.000 o más: LÍDER
                """);
    }

    private static String describirPersona(Persona persona) {
        return switch (persona) {
            case Desarrollador des -> "Desarrollador de " + des.getLenguajePrincipal();
            case Gerente ger -> "Gerente con presupuesto " + ger.getPresupuestoMensual();
            case ConsultorExterno con -> "Consultor de " + con.getEmpresaContratante();
        };
    }
}