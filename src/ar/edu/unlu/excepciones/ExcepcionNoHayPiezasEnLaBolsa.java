package ar.edu.unlu.excepciones;

public class ExcepcionNoHayPiezasEnLaBolsa extends RuntimeException {

    public void mensaje(){
        System.out.printf("error,no hay mas fichas en la bolsa");
    }
}
