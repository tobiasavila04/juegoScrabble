package ar.edu.unlu.excepciones;

public class ExcepcionCoordenadaIncorrecta extends RuntimeException {
    private int x;
    private int y;

    public ExcepcionCoordenadaIncorrecta(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void mensajeError(){
        System.out.printf("error, las coordenadas %d-%d son incorrectas", (char)(x + 'A'), (y + 1));
    }
}
