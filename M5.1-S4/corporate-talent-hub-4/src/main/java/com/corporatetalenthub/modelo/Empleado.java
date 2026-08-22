package com.corporatetalenthub.modelo;

/**
 * Empleado también se declara "sealed": dentro del dominio "Empleado" hoy
 * solo existen dos roles válidos (Desarrollador y Gerente). Igual que en
 * Persona, esto impide que cualquier otra clase "inserte" un rol no
 * controlado en la jerarquía de nómina/desempeño, y habilita switches
 * exhaustivos con pattern matching sobre Empleado.
 */
public abstract sealed class Empleado extends Persona permits Desarrollador, Gerente {

    private final double salario;
    protected double promedioDesempenio; // protected: las subclases lo leen para calcular su bono

    protected Empleado(int id, String nombre, byte edad, double salario) {
        super(id, nombre, edad);
        this.salario = salario;
    }

    public double getSalario() {
        return salario;
    }

    public void setPromedioDesempenio(double promedioDesempenio) {
        this.promedioDesempenio = promedioDesempenio;
    }

    public double getPromedioDesempenio() {
        return promedioDesempenio;
    }
}