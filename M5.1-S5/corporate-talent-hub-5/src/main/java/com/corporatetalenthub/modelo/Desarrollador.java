package com.corporatetalenthub.modelo;

public final class Desarrollador extends Empleado implements Promocionable {

    private final String lenguajePrincipal;

    public Desarrollador(int id, String nombre, byte edad, double salario, String lenguajePrincipal) {
        super(id, nombre, edad, salario);
        this.lenguajePrincipal = lenguajePrincipal;
    }

    public String getLenguajePrincipal() {
        return lenguajePrincipal;
    }

    @Override
    public double calcularBonoAscenso() {
        // Bono simple: 10% del salario si el desempeño es alto, 5% si no.
        return getSalario() * (promedioDesempenio >= 80.0 ? 0.10 : 0.05);
    }
}