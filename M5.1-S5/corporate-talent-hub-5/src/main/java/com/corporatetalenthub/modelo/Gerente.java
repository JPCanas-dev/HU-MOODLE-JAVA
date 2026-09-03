package com.corporatetalenthub.modelo;

public final class Gerente extends Empleado implements Promocionable {

    private final double presupuestoMensual;

    public Gerente(int id, String nombre, byte edad, double salario, double presupuestoMensual) {
        super(id, nombre, edad, salario);
        this.presupuestoMensual = presupuestoMensual;
    }

    public double getPresupuestoMensual() {
        return presupuestoMensual;
    }

    @Override
    public double calcularBonoAscenso() {
        // Bono ligado al presupuesto que gestiona, más un extra si el desempeño es alto.
        return (presupuestoMensual * 0.02) + (promedioDesempenio >= 80.0 ? 500_000 : 0);
    }
}