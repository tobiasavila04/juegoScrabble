package ar.edu.unlu.Modelo;

import java.util.ArrayList;

public class Palabra {
    private int puntaje;
    private int comienzoX;
    private int comienzoY;
    private int finX;
    private int finY;
    private Tablero tablero;
    private Diccionario diccionario;

    public Palabra() {
        this.tablero = new Tablero();
        this.diccionario = new Diccionario();
    }

    public Boolean esHorizontal() {
        return this.comienzoY == this.finY;
    }

    public Boolean esVertical() {
        return this.comienzoX == this.finX;
    }

    public boolean validarPalabra(ArrayList<PosicionTablero> coordenadasColocadas) {
        StringBuilder palabraHorizontal = new StringBuilder(); // Palabra en dirección horizontal
        StringBuilder palabraVertical = new StringBuilder();   // Palabra en dirección vertical
        boolean esValidaHorizontal = true; // Flag de validación horizontal
        boolean esValidaVertical = true;   // Flag de validación vertical

        // Recorrer las coordenadas colocadas
        for (int i = 0; i < coordenadasColocadas.size(); i++) {
            PosicionTablero posicion = coordenadasColocadas.get(i);
            Ficha ficha = tablero.obtenerContenidoCelda(posicion);


            // Verificar si la palabra es horizontal
            palabraHorizontal.append(ficha.getLetra());
            if (i > 0) { // A partir de la segunda coordenada, verificar si están alineadas horizontalmente
                PosicionTablero posicionAnterior = coordenadasColocadas.get(i - 1);
                if (posicion.getPosicionX() != posicionAnterior.getPosicionX() + 1) {
                    esValidaHorizontal = false; // Si no están alineadas horizontalmente
                }
            }

            // Verificar si la palabra es vertical
            palabraVertical.append(ficha.getLetra());
            if (i > 0) { // A partir de la segunda coordenada, verificar si están alineadas verticalmente
                PosicionTablero posicionAnterior = coordenadasColocadas.get(i - 1);
                if (posicion.getPosicionY() != posicionAnterior.getPosicionY() + 1) {
                    esValidaVertical = false; // Si no están alineadas verticalmente
                }
            }
        }

        // Validación de la palabra principal (horizontal o vertical)
        if (esValidaHorizontal && (!palabraHorizontal.isEmpty())) {
            boolean esValida = diccionario.existePalabra(palabraHorizontal.toString()); // Verificar si la palabra existe
            if (!esValida) {
                System.out.println("La palabra horizontal no es válida.");
            } else {
                System.out.println("La palabra horizontal es válida: " + palabraHorizontal);
            }
        }

        if (esValidaVertical && (!palabraVertical.isEmpty())) {
            boolean esValida = diccionario.existePalabra(palabraVertical.toString()); // Verificar si la palabra existe
            if (!esValida) {
                System.out.println("La palabra vertical no es válida.");
            } else {
                System.out.println("La palabra vertical es válida: " + palabraVertical);
            }
        }

        // Ahora verificamos las palabras formadas por las celdas adyacentes
        for (int i = 0; i < coordenadasColocadas.size(); i++) {
            PosicionTablero posicion = coordenadasColocadas.get(i);
            Ficha ficha = tablero.obtenerContenidoCelda(posicion);

            // Verificar las palabras que se forman horizontalmente (si estamos colocando una ficha vertical)
            if (esValidaHorizontal) {
                String palabraHorizontalAdj = formarPalabraHorizontal(posicion);
                if (!diccionario.existePalabra(palabraHorizontalAdj)) {
                    System.out.println("Palabra horizontal adyacente no válida: " + palabraHorizontalAdj);
                    return false;
                } else {
                    System.out.println("Palabra horizontal adyacente válida: " + palabraHorizontalAdj);
                    return true;
                }
            }

            // Verificar las palabras que se forman verticalmente (si estamos colocando una ficha horizontal)
            if (esValidaVertical) {
                String palabraVerticalAdj = formarPalabraVertical(posicion);
                if (!diccionario.existePalabra(palabraVerticalAdj)) {
                    System.out.println("Palabra vertical adyacente no válida: " + palabraVerticalAdj);
                    return false;
                } else {
                    System.out.println("Palabra vertical adyacente válida: " + palabraVerticalAdj);
                    return true;
                }
            }
        }
        return esValidaHorizontal;
    }

    private String formarPalabraHorizontal(PosicionTablero posicion) {
        StringBuilder palabra = new StringBuilder();
        int x = posicion.getPosicionX();
        int y = posicion.getPosicionY();

        // Expandir hacia la izquierda
        for (int i = x; i >= 0 && tablero.obtenerContenidoCelda(new PosicionTablero(i, y)) != null; i--) {
            palabra.insert(0, tablero.obtenerContenidoCelda(new PosicionTablero(i, y)).getLetra());
        }

        // Expandir hacia la derecha
        for (int i = x + 1; i < 15 && tablero.obtenerContenidoCelda(new PosicionTablero(i, y)) != null; i++) {
            palabra.append(tablero.obtenerContenidoCelda(new PosicionTablero(i, y)).getLetra());
        }

        return palabra.toString();
    }

    private String formarPalabraVertical(PosicionTablero posicion) {
        StringBuilder palabra = new StringBuilder();
        int x = posicion.getPosicionX();
        int y = posicion.getPosicionY();

        // Expandir hacia arriba
        for (int i = y; i >= 0 && tablero.obtenerContenidoCelda(new PosicionTablero(x, i)) != null; i--) {
            palabra.insert(0, tablero.obtenerContenidoCelda(new PosicionTablero(x, i)).getLetra());
        }

        // Expandir hacia abajo
        for (int i = y + 1; i < 15 && tablero.obtenerContenidoCelda(new PosicionTablero(x, i)) != null; i++) {
            palabra.append(tablero.obtenerContenidoCelda(new PosicionTablero(x, i)).getLetra());
        }

        return palabra.toString();
    }


    public String calcularPalabra(Tablero tablero){
        StringBuilder palabra = new StringBuilder();
        if (this.esHorizontal()) {
            for (int i = this.comienzoX; i <= this.finX; i++) {
                Celda celda = tablero.getCelda(new PosicionTablero(i,comienzoY));
                if (!celda.isVacia()) {
                    palabra.append(celda.getFicha());
                } else {
                    palabra = new StringBuilder("Palabra inválida o está vacía. Tal vez ingresó mal la posición. Intente en su siguiente turno");
                }
            }
        } else {
            for (int i = this.comienzoY; i <= this.finY; i++) {
                Celda celda = tablero.getCelda(new PosicionTablero(comienzoX, i));
                if (!celda.isVacia()) {
                    palabra.append(celda.getFicha());
                } else {
                    palabra = new StringBuilder("Palabra inválida o está vacía");
                }
            }
        }
        return palabra.toString();
    }



    public int PuntajePalabra(ArrayList<PosicionTablero> coordenadasColocadas) {
        int puntajeTotal = 0;
        boolean esHorizontal = esHorizontal(coordenadasColocadas);

        puntajeTotal += calcularPuntajePrincipal(coordenadasColocadas, esHorizontal);

        // Sumar el puntaje de las palabras adyacentes
        for (PosicionTablero posicion : coordenadasColocadas) {
            if (esHorizontal) {
                puntajeTotal += calcularPuntajeAdyacenteVertical(posicion);
            } else {
                puntajeTotal += calcularPuntajeAdyacenteHorizontal(posicion);
            }
        }
        return puntajeTotal;
    }

    private int calcularPuntajePrincipal(ArrayList<PosicionTablero> coordenadasColocadas, boolean esHorizontal) {
        int puntaje = 0;
        int multiplicadorPalabra = 1;

        for (PosicionTablero posicion : coordenadasColocadas) {
            Celda celda = tablero.getCelda(posicion);
            Ficha ficha = celda.getFicha();

            if (ficha != null) {
                int valorFicha = valorSegunCelda(celda);
                puntaje += valorFicha;
                multiplicadorPalabra += multiplicadorCeldaPalabra(celda);
            }
        }
        return puntaje * multiplicadorPalabra;
    }

    private int calcularPuntajeAdyacenteVertical(PosicionTablero posicion) {
        int puntaje = 0;
        int multiplicadorPalabra = 1;
        boolean tienePalabra = false;

        // Expandir hacia arriba
        for (int y = posicion.getPosicionY() - 1; y >= 0; y--) {
            PosicionTablero posArriba = new PosicionTablero(posicion.getPosicionX(), y);
            Celda celda = tablero.getCelda(posArriba);
            if (celda.getFicha() == null) break;
            puntaje += celda.getFicha().getPuntos();
            tienePalabra = true;
        }

        // Incluir la ficha actual
        Celda celdaActual = tablero.getCelda(posicion);
        puntaje += celdaActual.getFicha().getPuntos();

        // Expandir hacia abajo
        for (int y = posicion.getPosicionY() + 1; y < 225; y++) {
            PosicionTablero posAbajo = new PosicionTablero(posicion.getPosicionX(), y);
            Celda celda = tablero.getCelda(posAbajo);
            if (celda.getFicha() == null) break;
            puntaje += celda.getFicha().getPuntos();
            tienePalabra = true;
        }

        return tienePalabra ? puntaje * multiplicadorPalabra : 0;
    }

    private int calcularPuntajeAdyacenteHorizontal(PosicionTablero posicion) {
        int puntaje = 0;
        int multiplicadorPalabra = 1;
        boolean tienePalabra = false;

        // Expandir hacia la izquierda
        for (int x = posicion.getPosicionX() - 1; x >= 0; x--) {
            PosicionTablero posIzquierda = new PosicionTablero(x, posicion.getPosicionY());
            Celda celda = tablero.getCelda(posIzquierda);
            if (celda.getFicha() == null) break;
            puntaje += celda.getFicha().getPuntos();
            tienePalabra = true;
        }

        // Incluir la ficha actual
        Celda celdaActual = tablero.getCelda(posicion);
        puntaje += celdaActual.getFicha().getPuntos();

        // Expandir hacia la derecha
        for (int x = posicion.getPosicionX() + 1; x <225; x++) {
            PosicionTablero posDerecha = new PosicionTablero(x, posicion.getPosicionY());
            Celda celda = tablero.getCelda(posDerecha);
            if (celda.getFicha() == null) break;
            puntaje += celda.getFicha().getPuntos();
            tienePalabra = true;
        }

        return tienePalabra ? puntaje * multiplicadorPalabra : 0;
    }

    private boolean esHorizontal(ArrayList<PosicionTablero> coordenadasColocadas) {
        // Verificar si todas las posiciones tienen la misma coordenada Y
        int yInicial = coordenadasColocadas.get(0).getPosicionY();
        for (PosicionTablero posicion : coordenadasColocadas) {
            if (posicion.getPosicionY() != yInicial) {
                return false;
            }
        }
        return true;
    }







    private int valorSegunCelda(Celda celda) {
        Ficha ficha = celda.getFicha();
        int valorBase = ficha.getPuntos();

        switch (celda.getTipo()) {
            case DOBLE_LETRA:
                return valorBase * 2;
            case TRIPLE_LETRA:
                return valorBase * 3;
            default:
                return valorBase;
        }

    }

    private int multiplicadorCeldaPalabra(Celda celda) {
        switch (celda.getTipo()) {
            case DOBLE_PALABRA:
                return 2;
            case TRIPLE_PALABRA:
                return 3;
            default:
                return 1;
        }
    }
}
