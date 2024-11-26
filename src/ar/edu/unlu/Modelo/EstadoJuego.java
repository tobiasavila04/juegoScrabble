package ar.edu.unlu.Modelo;

import ar.edu.unlu.Enums.Eventos;

public class EstadoJuego {

    private final Eventos evento;
    private final Jugador turnoActual;

    public EstadoJuego(Eventos evento, Jugador turnoActual) {
        this.evento = evento;
        this.turnoActual = turnoActual;
    }

    public Eventos getEvento() {
        return evento;
    }

    public Jugador getTurnoActual() {
        return turnoActual;
    }
}
