package com.corporatetalenthub.modelo;

/**
 * Modelo tradicional compatible con la sintaxis de Java 8.
 *
 * Esta clase es más verbosa que un Record porque declara campos, constructor,
 * getters, setter y métodos explícitamente. Esa verbosidad es útil cuando el
 * objeto necesita estado mutable, como bonoMensual o nombre.
 */
public class Empleado {

    // Los 8 tipos primitivos requeridos:
    private byte nivelAcceso;
    private short anioIngreso;
    private int idEmpleado;
    private long numeroDocumento;
    private float puntajeTest;
    private double salarioBase;
    private char tipoContrato;
    private boolean esActivo;

    // String no es primitivo: es una clase y esta variable guarda una referencia.
    private String nombre;

    // Datos adicionales necesarios para las reglas de negocio.
    private int edad;
    private int idSede;
    private double bonoMensual;

    public Empleado(
            byte nivelAcceso,
            short anioIngreso,
            int idEmpleado,
            long numeroDocumento,
            float puntajeTest,
            double salarioBase,
            char tipoContrato,
            boolean esActivo,
            String nombre,
            int edad,
            int idSede,
            double bonoMensual) {
        this.nivelAcceso = nivelAcceso;
        this.anioIngreso = anioIngreso;
        this.idEmpleado = idEmpleado;
        this.numeroDocumento = numeroDocumento;
        this.puntajeTest = puntajeTest;
        this.salarioBase = salarioBase;
        this.tipoContrato = tipoContrato;
        this.esActivo = esActivo;
        this.nombre = nombre;
        this.edad = edad;
        this.idSede = idSede;
        this.bonoMensual = bonoMensual;
    }

    public double calcularSalarioFinal() {
        // Orden: paréntesis internos -> multiplicaciones -> suma -> resta.
        // 1. bonoMensual * 1.10
        // 2. salarioBase + resultado anterior
        // 3. salarioBase * 0.05
        // 4. resta de ambos resultados
        return (salarioBase + (bonoMensual * 1.10)) - (salarioBase * 0.05);
    }
    
    public boolean tienenBonoExtra() {
        // % se evalúa antes que ==. Un residuo 0 indica que el ID es par.
        return idEmpleado % 2 == 0;
    }
    
    public boolean validarElegibilidad() {
        // Precedencia:primero !, después && y finalmente ||.
        // Los paréntesis hacen explícitos los dos grupos de condiciones.
        return (puntajeTest > 85 && edad < 30) || (idSede == 1 && !esActivo);
    }
    
    public void actualizarBonoMensual(double incremento) {
        // Equivale a: bonoMensual = bonoMensual + incremento;
        bonoMensual += incremento;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getIdEmpleado() {
        return this.idEmpleado;
    }
    
    public double getBonoMensual() {
        return bonoMensual;
    }
    
    // Java mostraría algo como "com.corporatetalenthub.modelo.Empleado@4f3f5b24" cuando
    // se vaya a imprimir el objeto Empleado en el System.out.print() de App.
    
    // Por eso se sobrescribe (@Override) el método toString(). Par que java muestre el
    // mensaje que yo le especifique, en lugar del predeterminado.

    @Override
    public String toString() {
        return "Empleado {\n" +
                "nivelAcceso = " + nivelAcceso + ",\n" + 
                "anioIngreso = " + anioIngreso + ",\n" + 
                "idEmpleado = " + idEmpleado + ",\n" + 
                "numeroDocumento = " + numeroDocumento + ",\n" + 
                "puntajeTest = " + puntajeTest + ",\n" + 
                "salarioBase = " + salarioBase + ",\n" + 
                "tipoContrato = " + tipoContrato + ",\n" + 
                "esActivo = " + esActivo + ",\n" + 
                "nombre = " + esActivo + ",\n" + 
                "edad = " + edad + ",\n" + 
                "idSede = " + idSede + ",\n" + 
                "bonoMensual = " + bonoMensual + ",\n" + 
                '}' + "\n";
    }
    
    
}
