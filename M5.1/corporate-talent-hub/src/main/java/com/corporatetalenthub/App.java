package com.corporatetalenthub;

import com.corporatetalenthub.modelo.Empleado;
import com.corporatetalenthub.modelo.EmpresaRecord;

/**
 *
 * @author cohorte5
 */
public class App {

    public static void main(String[] args) {

        String encabezado = """
                =====================================
                     CORPORATE TALENT HUB
                   Gestión del talento humano
                =====================================
                """;
        System.out.println(encabezado);

        Empleado empleado = crearEmpleadoDePrueba();
        EmpresaRecord empresa = new EmpresaRecord(
                "CodeUp solutions",
                "900123456-7",
                2015);

        System.out.println(empleado);
        System.out.println("Empresa: " + empresa.nombre());
        System.out.println("Salario final: " + empleado.calcularSalarioFinal());
        System.out.println("¿ID par con bono extra?: " + empleado.tienenBonoExtra());
        System.out.println("¿Empleado elegible?: " + empleado.validarElegibilidad());

        if (empleado.tienenBonoExtra()) {
            empleado.actualizarBonoMensual(100_000.0);
            System.out.println("Bono actualizado con += " + empleado.getBonoMensual());
            // Mostrar el salario sumando el bono extra
            System.out.println("Salario final + bono extra: " + empleado.calcularSalarioFinal());
        }

        compararReferencias();
        ejecutarLaboratiorioDeNodulos(empleado);

        // EJEMPLO DE COMPARACION DE OBJETOS CON RECORD
        EmpresaRecord a = new EmpresaRecord("CodeUp", "123", 2015);
        EmpresaRecord b = new EmpresaRecord("CodeUp", "123", 2015);
        
        System.out.println("");
        System.out.println(a);
        System.out.println(b);
        System.out.println(a == b);      // false: referencias diferentes
        System.out.println(a.equals(b)); // true: componentes iguales

    }

    // DEBE estar fuera del método main. NO se pueden declarar métodos dentro de otros métodos.
    // Static: se accede a un método de la clase, sin necesidad de crear/instanciar un objeto
    private static Empleado crearEmpleadoDePrueba() {
        return new Empleado(
                (byte) 3, // byte
                (short) 2024, // short
                102, // int: ID par
                1_023_456_789L, // long: sufijo L
                92.5f, // float: sufijo f
                3_000_000.0, // double
                'I', // char: contrato indefinido
                true, // boolean
                "Laura Gómez", // String
                27,
                2,
                500_000.0);
    }

    private static void compararReferencias() {
        Empleado primero = crearEmpleadoDePrueba();
        Empleado segundo = crearEmpleadoDePrueba();
        Empleado aliasDelPrimero = primero;

        System.out.println("primero == segundo: " + (primero == segundo));
        System.out.println("primero == AliasDelprimero: " + (primero == aliasDelPrimero));

    }

    private static void ejecutarLaboratiorioDeNodulos(Empleado empleado) {
        empleado.setNombre(null);
        
        // Decir que null elimina inmediatamente el objeto: solo elimina una referencia.

        try {
            System.out.println(empleado.getNombre().toUpperCase());
        } catch (NullPointerException excepcion) {
            System.out.println("NPE controlada: " + excepcion.getMessage());
        }

        // Java 8 normalmente informa que ocurrió una NullPointerException y señala
        // la línea mediante el stack trace, pero una expresión encadenada puede hacer
        // difícil reconocer cuál referencia era null.
        // Desde Java 14, Helpful NullPointerExceptions puede indicar que no se pudo
        // invocar toUpperCase() porque el resultado de getNombre() era null.
        // El try/catch es solo para que el laboratorio no detenga toda la aplicación;
        // la solución real es validar el dato o impedir nombres nulos según el dominio.
    }

}
