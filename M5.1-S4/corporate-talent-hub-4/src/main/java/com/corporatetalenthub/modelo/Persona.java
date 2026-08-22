package com.corporatetalenthub.modelo;

/**
 * TASK 1 - Herencia sellada vs abierta.
 *
 * ESTILO LEGACY (Java 8/11):
 *   public abstract class Persona { ... }
 *   Con esta declaración, CUALQUIER clase, de cualquier paquete o módulo,
 *   podía extender Persona libremente:
 *
 *       public class Mascota extends Persona { ... } // ¡nada lo impedía!
 *
 *   El diseñador de la API perdía el control del dominio del negocio.
 *   Además, cualquier código que hiciera "instanceof" sobre Persona nunca
 *   podía estar seguro de haber cubierto todos los casos posibles, porque
 *   en cualquier momento podía aparecer un subtipo no previsto.
 *
 * ESTILO MODERNO (Java 17/21) - Sealed Classes:
 *   Con "sealed ... permits" el propio COMPILADOR garantiza que SOLO
 *   Empleado y ConsultorExterno pueden extender Persona. Ventajas frente
 *   a la herencia abierta:
 *     1. Seguridad de API: el dominio queda cerrado y documentado en el
 *        propio código fuente; no hay subtipos "sorpresa" en tiempo de
 *        ejecución ni en otros módulos del proyecto.
 *     2. Exhaustividad: un switch con pattern matching sobre Persona puede
 *        omitir la rama "default", porque el compilador conoce todos los
 *        subtipos posibles y avisa en tiempo de compilación si falta
 *        cubrir alguno (ver App.describirPersona).
 *     3. Mejor mantenibilidad: agregar un nuevo tipo de Persona obliga a
 *        modificar esta clase explícitamente (la lista de "permits"),
 *        evitando efectos colaterales ocultos en otras partes del sistema.
 */
public abstract sealed class Persona permits Empleado, ConsultorExterno {

    private final int id;
    private final String nombre;
    protected final byte edad; // protected: las subclases acceden directo, sin necesidad de getter

    protected Persona(int id, String nombre, byte edad) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public byte getEdad() {
        return edad;
    }
}