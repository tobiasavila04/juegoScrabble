package ar.edu.unlu.Modelo;

import ar.edu.unlu.Enums.EstadoCelda;
import ar.edu.unlu.Enums.Eventos;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IScrabbleGame extends IObservableRemoto {
    void inicializarJuego() throws RemoteException ;

    Tablero getTablero() throws RemoteException;
    Bolsa getBolsa() throws RemoteException;

    Jugador inicializarPrimerTurno(Bolsa bolsa) throws RemoteException;

    Jugador getPrimerTurno() throws RemoteException;

    boolean esTurnoDe(Jugador jugador) throws RemoteException;

    void calcularSiguienteTurno() throws RemoteException;

    Jugador getJugadorActual() throws RemoteException;
    ArrayList<Jugador> getJugadores() throws RemoteException;

    void setTurnoActual(Jugador turno)throws RemoteException ;

    Jugador getTurnoActual()throws RemoteException;

    void generarAtril(Jugador jugador) throws RemoteException;

    boolean terminoJuego() throws RemoteException;

    void colocarFichaEnCelda(String letra, PosicionTablero posicion) throws RemoteException;
    boolean verificarNombreJugador(String nombre) throws RemoteException;
    void conectarJugador(Jugador jugador) throws RemoteException;

    boolean validarOpcionJugadorMenuPrincipal(int opcion) throws RemoteException;
    Boolean validarCantidadJugadores(int cantidadJugadores) throws RemoteException;
    Boolean validarPosicionTablero(PosicionTablero posicion)throws RemoteException;


    boolean celdaLibreYvalida(PosicionTablero posicion) throws RemoteException;

    Ficha obtenerContenidoCelda(PosicionTablero posicion) throws RemoteException;

    String obtenerIndiceCeldaTablero(PosicionTablero posicion) throws RemoteException;
    ArrayList<Ficha> obtenerFichasDelAtril(Jugador jugador) throws RemoteException;


    int getCantidadDeJugadores() throws RemoteException;

    void cantJugadores(int cantidadJugadores) throws RemoteException;

    Jugador desconectarJugador(Jugador jugador) throws RemoteException;

    void setCantidadDeJugadores(int cantidadJugadores) throws RemoteException;

    ArrayList<Ficha> cambiarFichas(String idJugador, ArrayList<Integer> indicesFichasACambiar) throws RemoteException;

    void pasarTurno() throws RemoteException;

    void guardarCoordenadas(PosicionTablero posicion) throws RemoteException;

    boolean validarPalabra() throws RemoteException;

    int calculaPuntaje() throws RemoteException;

    void completarAtril() throws RemoteException;
}