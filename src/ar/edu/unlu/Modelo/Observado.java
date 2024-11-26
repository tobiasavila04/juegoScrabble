package ar.edu.unlu.Modelo;

import ar.edu.unlu.Enums.Eventos;

public interface Observado {
    void agregarObservador(Observado observador);

    void notificarObservador(Eventos eventos);
}
