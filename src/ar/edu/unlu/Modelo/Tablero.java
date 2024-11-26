package ar.edu.unlu.Modelo;

import ar.edu.unlu.Enums.BonusTablero;
import ar.edu.unlu.Enums.EstadoCelda;
import ar.edu.unlu.Enums.Eventos;

import java.io.Serializable;
import java.util.ArrayList;

public class Tablero implements Serializable {
    private ArrayList<Celda> celdasTablero;

    public Tablero() {
        celdasTablero = new ArrayList<>();
        inicializarTablero();
    }

    public ArrayList<Celda> getCeldas() {
        return celdasTablero;
    }

    public Celda getCelda(PosicionTablero posicion) {
        int indice = posicion.getPosicionX() * 15 + posicion.getPosicionY();
        return celdasTablero.get(indice);
    }

    private void inicializarTablero() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                BonusTablero tipo = obtenerValoresEspeciales(i, j);
                celdasTablero.add(new Celda(new PosicionTablero(i, j), tipo));
            }
        }
    }

    private BonusTablero obtenerValoresEspeciales(int x, int y) {
        // Coordenadas de las bonificaciones basadas en el tablero oficial de Scrabble.
        if ((x == 0 && y == 0) || (x == 0 && y == 7) || (x == 0 && y == 14) ||
                (x == 7 && y == 0) || (x == 7 && y == 14) ||
                (x == 14 && y == 0) || (x == 14 && y == 7) || (x == 14 && y == 14)) {
            return BonusTablero.TRIPLE_PALABRA;
        }
        if ((x == 1 && y == 1) || (x == 2 && y == 2) || (x == 3 && y == 3) ||
                (x == 4 && y == 4) || (x == 10 && y == 10) || (x == 11 && y == 11) ||
                (x == 12 && y == 12) || (x == 13 && y == 13) || (x == 1 && y == 13) || (x == 13 && y == 1) || (x==12 && y == 2) || (x == 11 && y == 3) || (x == 10 && y == 4) ||
                (x == 2 && y == 12) || (x == 3 && y == 11) || (x == 4 && y == 10) || (x == 7 && y == 7)) {
            return BonusTablero.DOBLE_PALABRA;
        }
        if ((x == 1 && y == 5) || (x == 1 && y == 9) || (x == 5 && y == 1) ||
                (x == 5 && y == 5) || (x == 5 && y == 9) || (x == 5 && y == 13) ||
                (x == 9 && y == 1) || (x == 9 && y == 5) || (x == 9 && y == 9) ||
                (x == 9 && y == 13) || (x == 13 && y == 5) || (x == 13 && y == 9)) {
            return BonusTablero.TRIPLE_LETRA;
        }
        if ((x == 0 && y == 3) || (x == 0 && y == 11) || (x == 2 && y == 6) ||
                (x == 2 && y == 8) || (x == 3 && y == 0) || (x == 3 && y == 7) ||
                (x == 3 && y == 14) || (x == 6 && y == 2) || (x == 6 && y == 6) ||
                (x == 6 && y == 8) || (x == 6 && y == 12) || (x == 7 && y == 3) ||
                (x == 7 && y == 11) || (x == 8 && y == 2) || (x == 8 && y == 6) ||
                (x == 8 && y == 8) || (x == 8 && y == 12) || (x == 11 && y == 0) ||
                (x == 11 && y == 7) || (x == 11 && y == 14) || (x == 12 && y == 6) ||
                (x == 12 && y == 8) || (x == 14 && y == 3) || (x == 14 && y == 11)) {
            return BonusTablero.DOBLE_LETRA;
        }
        return BonusTablero.NORMAL;
    }


    public EstadoCelda obtenerEstadoCelda(PosicionTablero posicion) {
        Celda celdaBuscada = null;
        for (Celda celda : celdasTablero) {
            if (celda.getPosicion().equals(posicion)) {
                celdaBuscada = celda;
            }
        }
        if (celdaBuscada != null) {
            return celdaBuscada.getEstado();
        }
        return null;
    }

    public Ficha obtenerContenidoCelda(PosicionTablero posicion) {
        Celda celdaBuscada = null;
        for (Celda celda : celdasTablero) {
            if (celda.getPosicion().equals(posicion)) {
                celdaBuscada = celda;
            }
        }
        if (celdaBuscada != null) {
            return celdaBuscada.getFicha();
        }
        return null;
    }


    public String obtenerBonificacionCelda(PosicionTablero posicion) {
        Celda celdaBuscada = null;
        for (Celda celda : celdasTablero) {
            if (celda.getPosicion().equals(posicion)) {
                celdaBuscada = celda;
            }
        }
        if (celdaBuscada != null) {
            return celdaBuscada.celdaBonificacion();
        }
        return null;
    }

    public boolean validarCelda(PosicionTablero posicion) {
        Celda celda = getCelda(posicion); // Busca la celda en el mapa
        if (celda == null) {
            return false; // La posición está fuera del tablero
        }
        return celda.getEstado() == EstadoCelda.LIBRE; // Valida si la celda está libre
    }


    public Celda obtenerCelda(PosicionTablero posicion) {
        // Asegúrate de que las coordenadas estén dentro del rango válido (0 <= posicionX < 15 y 0 <= posicionY < 15)
        int posicionX = posicion.getPosicionX();
        int posicionY = posicion.getPosicionY();

        // Verificamos que las coordenadas sean válidas
        if (posicionX < 0 || posicionX >= 15 || posicionY < 0 || posicionY >= 15) {
            throw new IllegalArgumentException("Coordenadas fuera del rango válido.");
        }

        // Calcular el índice en el ArrayList basado en las coordenadas (fila, columna)
        int index = posicionX * 15 + posicionY;

        // Devolver la celda correspondiente
        return celdasTablero.get(index);
    }
}



