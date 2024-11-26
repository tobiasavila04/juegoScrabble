package ar.edu.unlu.Modelo;

import java.io.Serializable;

public class Ficha  implements Serializable {
    private String letra;
    private int puntos;

    public Ficha(String letra, int puntos){
        this.letra = letra;
        this.puntos = puntos;
    }

    public Ficha(String letra){
        this.letra = letra;
    }

    public String getLetra() {
        return letra;
    }
    public int getPuntos() {
        return puntos;
    }

    public String toString() {
        return String.valueOf(letra);  // Devuelve la letra de la ficha como String
    }

}
