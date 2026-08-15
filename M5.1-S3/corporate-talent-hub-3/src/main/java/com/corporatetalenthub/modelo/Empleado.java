package com.corporatetalenthub.modelo;

public class Empleado {
    
    private final int id;
    private final String nombre;
    
    /*
    Un byte puede almacenar valores entre -128 y 127. Y la HU nos pide validar la edad entre
    18 y 100. Un int también funciona, pero esto es para mostrar los usos de datos primtivos.
    
    Además, un dato byte (1 byte = 8 bits) ocupa menos espacio de memoria que int (4 bytes).
    */

    private final byte edad;
    private final double salario;
    
    // El promedio no es final porque todavía no existe cunado registramos el empleado
    private double promedioDesempenio;
    
    public Empleado(int id, String nombre, byte edad, double salario) {
        
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.salario = salario;
        
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getNombre() {
        return this.nombre;
    }
    
    public byte getEdad() {
        return this.edad;
    }
    
    public double getSalario() {
        return this.salario;
    }
    
    // Como los demás atributos son final, no es necesario crearles setters.
    
    public void setPromedioDesempenio(double promedioDesempenio) {
        this.promedioDesempenio = promedioDesempenio;
    }

    public double getPromedioDesempenio() {
        return this.promedioDesempenio;
    }

}
