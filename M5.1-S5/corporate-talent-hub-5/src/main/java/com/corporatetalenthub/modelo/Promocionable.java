package com.corporatetalenthub.modelo;

import java.time.LocalDateTime;

/**
 * TASK 4 - Abstracción y evolución de interfaces.
 *
 * calcularBonoAscenso() define el contrato que deben cumplir las clases
 * promocionables. Cada rol implementa su propia fórmula de bono, demostrando
 * polimorfismo mediante una interfaz.
 *
 * registrarLog(...) es un método default, incorporado en Java 8.
 * Los métodos default permiten agregar comportamiento a una interfaz sin
 * obligar a modificar todas las clases que ya la implementan.
 * En este caso, Desarrollador y Gerente heredan automáticamente este
 * comportamiento sin tener que implementarlo nuevamente.
 */
public interface Promocionable {

    double calcularBonoAscenso();

    default void registrarLog(String nombreEmpleado) {
        System.out.printf("[LOG %s] Bono de ascenso calculado para %s: %.2f%n",
                LocalDateTime.now(), nombreEmpleado, calcularBonoAscenso());
    }
}