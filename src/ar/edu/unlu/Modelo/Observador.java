package ar.edu.unlu.Modelo;

import ar.edu.unlu.Enums.Eventos;

public interface Observador {
    void notificar(Eventos evento);
}
