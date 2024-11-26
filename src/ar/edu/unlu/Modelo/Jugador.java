package ar.edu.unlu.Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Jugador  implements Serializable {
    private String nombre;
    private int puntuacion;
    private ArrayList<Ficha> ficha;
  //  private boolean conectado = true;
    private String id;

    public Jugador(String nombre) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.puntuacion = 0;
        this.ficha = new ArrayList<>();
    }


    public ArrayList<Ficha> getAtril() {
        return ficha;
    }

    public void setAtril(ArrayList<Ficha> ficha){
        this.ficha = ficha;
    }

    public void agregarPuntos(int puntos) {
        this.puntuacion += puntos;
    }

    public void generarAtril(Bolsa bolsa) {
        for (int i = 0; i < 7; i++) {  // Supongamos que cada jugador tiene 7 fichas en el atril
            Ficha ficha = bolsa.sacarFichaDeLaBolsa();  // Sacamos una ficha de la bolsa
            this.ficha.add(ficha);
        }
    }

    public void completarAtril(Bolsa bolsa){
        ArrayList<Ficha> fichas = getAtril();
        for (int i = 0; i < 7; i++) {
            if(fichas.get(i) == null){
                Ficha ficha = bolsa.sacarFichaDeLaBolsa();  // Sacamos una ficha de la bolsa
                this.ficha.add(ficha);

            }
            // Supongamos que cada jugador tiene 7 fichas en el atril
        }
    }

    public String getId() {
        return id;
    }


    public String getNombre(){
        return nombre;
    }

}
