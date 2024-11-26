package ar.edu.unlu;

import ar.edu.unlu.Modelo.Bolsa;
import ar.edu.unlu.Modelo.Jugador;
import ar.edu.unlu.Modelo.Tablero;

import java.io.Serializable;
import java.util.ArrayList;

public class DatosPartidaGuardada implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Jugador> jugadores;
    private Tablero tablero;
    private Bolsa bolsa;
    private int turnoActual;

    public DatosPartidaGuardada(ArrayList<Jugador> jugadores, Tablero tablero, Bolsa bolsa, int turnoActual) {
        this.jugadores = jugadores;
        this.tablero = tablero;
        this.bolsa = bolsa;
        this.turnoActual = turnoActual;
    }

    // Getters y Setters
    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public Bolsa getBolsa() {
        return bolsa;
    }

    public void setBolsa(Bolsa bolsa) {
        this.bolsa = bolsa;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(int turnoActual) {
        this.turnoActual = turnoActual;
    }

    @Override
    public String toString() {
        return "DatosPartidaGuardada{" +
                "jugadores=" + jugadores +
                ", tablero=" + tablero +
                ", bolsa=" + bolsa +
                ", turnoActual=" + turnoActual +
                '}';
    }
}
