package ar.edu.unlu.Modelo;

import java.io.Serializable;

public class PosicionTablero  implements Serializable {
    private int posicionX;
    private int posicionY;

    public PosicionTablero(int posicionX,int posicionY){
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }
}
