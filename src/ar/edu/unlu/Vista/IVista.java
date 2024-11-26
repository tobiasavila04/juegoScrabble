package ar.edu.unlu.Vista;

import ar.edu.unlu.Enums.EstadoActual;
import ar.edu.unlu.Enums.Eventos;
import ar.edu.unlu.Modelo.Celda;
import ar.edu.unlu.Modelo.Ficha;
import ar.edu.unlu.Modelo.Jugador;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IVista {
    void mostrarTablero(ArrayList<Celda> celdas) throws RemoteException;
    void actualizarVista(EstadoActual estado);
    void mostrarTurnoJugador(Jugador jugador);

    void mostrarAtril() throws RemoteException;


    void habilitarSeleccionFicha();
    void habilitarSeleccionCelda(Ficha ficha) throws RemoteException;
    void deshabilitarSeleccionCeldaYFicha();
    void actualizarTablero() throws RemoteException;


    //void setControlador(ScrabbleControlador controlador) throws RemoteException;
    void iniciarVista() throws IOException;

    void actualizarTableroyAtril(Jugador jugador,ArrayList<Ficha> ficha, Eventos evento) throws RemoteException;

    void actualizarAtril(ArrayList<Ficha> ficha) throws RemoteException;

    void mostrarMenuTurno();

    void mostrarMensaje(String s);
    void mostrarJugadorConectado() throws RemoteException;

    public boolean verificarTurno() throws RemoteException;



    ArrayList<Integer> obtenerIndicesFichasSeleccionadas();

    void setJugadorLocal(Jugador jugador);

    Jugador getJugadorLocal();

    public void deshabilitarAtril();
    public void habilitarAtril();
    boolean esTurnoActual();
}
