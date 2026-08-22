package com.corporatetalenthub.modelo;

/**
 * TASK 2 - Modelado inmutable con records.
 *
 * ESTILO LEGACY (Java 8/11): para lograr lo mismo que este record en una
 * sola línea, había que escribir a mano algo así (POJO tradicional):
 *
 *     public final class DesempenoReportLegacy {
 *         private final int idEmpleado;
 *         private final double promedio;
 *         private final String feedback;
 *
 *         public DesempenoReportLegacy(int idEmpleado, double promedio, String feedback) {
 *             this.idEmpleado = idEmpleado;
 *             this.promedio = promedio;
 *             this.feedback = feedback;
 *         }
 *         public int getIdEmpleado() { return idEmpleado; }
 *         public double getPromedio() { return promedio; }
 *         public String getFeedback() { return feedback; }
 *
 *         @Override public boolean equals(Object o) { ... }   // ~10 líneas de boilerplate
 *         @Override public int hashCode() { ... }              // ~5 líneas
 *         @Override public String toString() { ... }           // ~3 líneas
 *     }
 *
 * ESTILO MODERNO (Java 17/21) - record: el compilador genera
 * automáticamente el constructor canónico, los accesores (idEmpleado(),
 * promedio(), feedback()), equals(), hashCode() y toString(). Además el
 * record es inmutable por diseño (no tiene setters), lo que lo hace
 * seguro para compartir entre reportes de fin de mes sin riesgo de que
 * alguien lo modifique después de haber sido emitido.
 */
public record DesempenioReport(int idEmpleado, double promedio, String feedback) {

    // Los "compact constructors" permiten validar sin repetir los parámetros.
    public DesempenioReport {
        if (promedio < 0.0 || promedio > 100.0) {
            throw new IllegalArgumentException("El promedio debe estar entre 0 y 100.");
        }
    }

    public static DesempenioReport de(Empleado empleado) {
        var promedio = empleado.getPromedioDesempenio();
        var feedback = promedio >= 80.0
                ? "Excelente desempeño, apto para promoción."
                : "Desempeño por debajo del objetivo trimestral.";
        return new DesempenioReport(empleado.getId(), promedio, feedback);
    }
}