package com.corporatetalenthub.modelo;

/**
 * Segundo (y único otro) subtipo permitido de Persona. Al ser "final",
 * cierra por completo esa rama de la jerarquía: nadie puede extenderla.
 */
public final class ConsultorExterno extends Persona {

    private final String empresaContratante;
    private final double tarifaPorHora;

    public ConsultorExterno(int id, String nombre, byte edad, String empresaContratante, double tarifaPorHora) {
        super(id, nombre, edad);
        this.empresaContratante = empresaContratante;
        this.tarifaPorHora = tarifaPorHora;
    }

    public String getEmpresaContratante() {
        return empresaContratante;
    }

    public double getTarifaPorHora() {
        return tarifaPorHora;
    }
}