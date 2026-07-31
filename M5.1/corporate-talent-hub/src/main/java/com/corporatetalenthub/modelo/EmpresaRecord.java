package com.corporatetalenthub.modelo;

/**
 * Un Record reduce la verbosidad: Java genera constructor, accesores,
 * equals, hashCode y toString a partir de sus componentes.
 *
 * Sus componentes son inmutables: después de construir el Record no se pueden
 * reasignar. La inmutabilidad es superficial; si un componente fuera un objeto
 * mutable, su contenido aún podría cambiar.
 */
public record EmpresaRecord(
        String nombre,
        String nit,
        int anioFundacion) {
}

/** 
* Un Record no genera métodos con nombres getNombre() o getNit(). 
* Sus accesores son nombre(), nit() y anioFundacion().
**/

