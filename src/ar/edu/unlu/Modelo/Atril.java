package ar.edu.unlu.Modelo;

import ar.edu.unlu.Enums.Eventos;

import java.io.Serializable;
import java.util.ArrayList;

public class Atril  implements Serializable {
    private int tamanio;
    private ArrayList<Ficha> fichas;

    public Atril(int tamanio) {
        this.tamanio = tamanio;
    }

    public void generarAtril(Bolsa bolsa) {
        for (int i = 0; i < 7; i++) {  // Supongamos que cada jugador tiene 7 fichas en el atril
            Ficha ficha = bolsa.sacarFichaDeLaBolsa();  // Sacamos una ficha de la bolsa
            agregarFichaAtril(ficha);
        }
    }



    public void agregarFichaAtril(Ficha ficha){
        this.fichas.add(ficha);
        //notificar();

    }

    public ArrayList<Ficha> getFicha(){
        return fichas;
    }

}
