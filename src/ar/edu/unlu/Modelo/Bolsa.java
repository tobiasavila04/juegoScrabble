package ar.edu.unlu.Modelo;

import ar.edu.unlu.excepciones.ExcepcionNoHayPiezasEnLaBolsa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Bolsa  implements Serializable {
    ArrayList<Ficha> fichas;

    public Bolsa(){
        fichas = new ArrayList<>();
        inicializarFichasEnLaBolsa();
        System.out.println("Fichas inicializadas: " + fichas.size());
    }

    public void inicializarFichasEnLaBolsa(){
        agregarFicha("A", 1, 12);
        agregarFicha("E", 1, 12);
        agregarFicha("O", 1, 9);
        agregarFicha("S", 1, 6);
        agregarFicha("I", 1, 6);
        agregarFicha("U", 1, 6);
        agregarFicha("N", 1, 5);
        agregarFicha("L", 1, 4);
        agregarFicha("R", 1, 5);
        agregarFicha("T", 1, 4);
        agregarFicha("C", 3, 4);
        agregarFicha("D", 2, 5);
        agregarFicha("G", 2, 2);
        agregarFicha("M", 3, 2);
        agregarFicha("B", 3, 2);
        agregarFicha("P", 3, 2);
        agregarFicha("F", 4, 1);
        agregarFicha("H", 4, 2);
        agregarFicha("V", 4, 2);
        agregarFicha("Y", 4, 1);
        agregarFicha("Q", 5, 1);
        agregarFicha("J", 8, 1);
        agregarFicha("Ñ", 8, 1);
        agregarFicha("X", 8, 1);
        agregarFicha("Z", 10, 1);
        agregarFicha("_", 0, 2); // Comodines
    }

    private void agregarFicha(String letra, int puntos, int cantidad){
        for(int i = 0; i < cantidad; i++){
            Ficha ficha = new Ficha(letra, puntos);
            fichas.add(ficha);
        }
    }

    public Ficha sacarFichaDeLaBolsa(){
        if (estaVacia()){
            return null;
        }
        Random rnd = new Random();
        int fichaRandom = rnd.nextInt(fichas.size());
        Ficha ficha = fichas.get(fichaRandom);
        fichas.remove(fichaRandom);
        System.out.println("Ficha sacada: " + ficha.getLetra());
        return ficha;
    }

    public ArrayList<Ficha> getFicha(){
        return fichas;
    }

    public void agregarFichas(ArrayList<Ficha> nuevasFichas) {
        fichas.addAll(nuevasFichas);
    }

    public void agregarFicha(Ficha ficha) {
        fichas.add(ficha);
    }

    public boolean estaVacia(){
        return (fichas.size() <= 0);
    }

}
