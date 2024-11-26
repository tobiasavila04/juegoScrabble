package ar.edu.unlu.Modelo;

import ar.edu.unlu.Enums.BonusTablero;
import ar.edu.unlu.Enums.EstadoCelda;
import ar.edu.unlu.excepciones.ExcepcionCoordenadaIncorrecta;

import java.io.Serializable;

public class Celda  implements Serializable {
    private Ficha ficha;
    private PosicionTablero posicion;
    private BonusTablero tipo;
    EstadoCelda estado;

    public Celda(PosicionTablero posicion, BonusTablero tipo) throws ExcepcionCoordenadaIncorrecta {
        this.posicion = posicion;
        this.tipo = tipo;
        this.estado = EstadoCelda.LIBRE;
    }


    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
        this.estado = (ficha == null) ? EstadoCelda.LIBRE : EstadoCelda.OCUPADA;
    }

    public BonusTablero getTipo() {
        return tipo;
    }

    public PosicionTablero getPosicion() {
        return posicion;
    }

    public EstadoCelda getEstado() {
        return estado;
    }

    public String celdaBonificacion() {
        if (ficha != null) {
            return ficha.getLetra();
        }
        switch (tipo) {
            case TRIPLE_PALABRA:
                return "TP";
            case DOBLE_PALABRA:
                return "DP";
            case TRIPLE_LETRA:
                return "TL";
            case DOBLE_LETRA:
                return "DL";
            default:
                return "..";
        }
    }

    public boolean isVacia() {
        return this.ficha == null;
        
    }
}
