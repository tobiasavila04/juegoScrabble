package ar.edu.unlu.Modelo;

import ar.edu.unlu.Enums.EstadoCelda;
import ar.edu.unlu.Enums.Eventos;
import ar.edu.unlu.Serializador;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Scrabblegame extends ObservableRemoto implements IScrabbleGame{
    private Jugador turnoActual;
    private int cantidadJugadores;
    private Tablero tablero;
    private Bolsa bolsa;
    private Atril atril;
    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private Palabra palabra;
    private Diccionario diccionario;
    private int turnosConsecutivosSinJugar;
    public static Scrabblegame instancia;
    private ArrayList<Jugador> jugadoresRegistrados;
    private ArrayList<PosicionTablero> coordenadasColocadas = new ArrayList<>();

    public Scrabblegame(){
        this.tablero = new Tablero();
        this.bolsa = new Bolsa();
        this.diccionario = new Diccionario();
        this.atril = new Atril(7);
        turnosConsecutivosSinJugar = 0;
        if (this.jugadoresRegistrados == null) {
            this.jugadoresRegistrados = new ArrayList<>();
        }else{
            this.jugadoresRegistrados = Serializador.cargarJugadorHistorico();
        }
    }

    public static synchronized Scrabblegame getInstancia() throws RemoteException{
        if (instancia == null){
            instancia = new Scrabblegame();
        }
        return instancia;
    }

    public void inicializarJuego() throws RemoteException {
        tablero = new Tablero();
        bolsa.inicializarFichasEnLaBolsa();
        diccionario.cargarDiccionario();
        repartirFichas();
    }

    public Tablero getTablero() throws RemoteException {
        return tablero;
    }

    public Bolsa getBolsa() throws RemoteException {
        return bolsa;
    }


    public int getCantidadDeJugadores() throws RemoteException{
        return cantidadJugadores;
    }

    @Override
    public void cantJugadores(int cantidadJugadores) throws RemoteException {

    }

    public void setCantJugadores(int cantJugadores)throws RemoteException{
        this.cantidadJugadores = cantJugadores;
    }

    @Override
    public Jugador desconectarJugador(Jugador jugador) throws RemoteException {
        if(this.jugadores.size() > 1) {
            this.jugadores.remove(jugador);
       //     notificarObservadores(Evento.JUGADOR_DESCONECTADO);
         //   notificarObservadores(Evento.CAMBIO_ESTADO_PARTIDA);
        }
        return jugador;
    }

    private void repartirFichas() {
        for (Jugador jugador : jugadores) {
            ArrayList<Ficha> atril = new ArrayList<>();
            for (int i = 0; i < 7; i++) { // Suponiendo un máximo de 7 fichas
                atril.add(bolsa.sacarFichaDeLaBolsa());
            }
            jugador.setAtril(atril); // Método en `Jugador` para asignar el atril
        }
    }



    public ArrayList<Ficha> cambiarFichas(String idJugador, ArrayList<Integer> FichasCambiar) {
        Jugador jugador = obtenerJugadorPorId(idJugador);
        if (jugador == null) {
            throw new IllegalArgumentException("Jugador no encontrado.");
        }
        ArrayList<Ficha> atril = jugador.getAtril();
        ArrayList<Ficha> fichasSeleccionadas = new ArrayList<>();
        for (int i : FichasCambiar) {
            Ficha ficha = atril.get(i);
            fichasSeleccionadas.add(ficha);
            atril.set(i, null); // Marcamos el espacio como vacío
        }
        bolsa.agregarFichas(fichasSeleccionadas); // Devolvemos las fichas al saco

        // Reemplazar las fichas seleccionadas por nuevas fichas del saco
        for (int i : FichasCambiar) {
            atril.set(i, bolsa.sacarFichaDeLaBolsa());
        }

        return new ArrayList<>(atril); // Retornar el nuevo estado del atril
    }

    public Jugador obtenerJugadorPorId(String idJugador) {
        for (Jugador jugador : jugadores) {
            if (jugador.getId().equals(idJugador)) {
                return jugador;  // Si el ID coincide, devuelve el jugador
            }
        }
        return null;  // Si no se encuentra el jugador, devuelve null
    }



    public Jugador inicializarPrimerTurno(Bolsa bolsa) throws RemoteException {
        Jugador jugadorQueEmpieza = null;
        int fichaMayorValor = -1;

        for (Jugador jugador : jugadores) {
            Ficha ficha = bolsa.sacarFichaDeLaBolsa();
            //jugador.setPrimerTurno(ficha);

            // Determina el jugador que empieza basándose en la ficha con mayor valor
            if (ficha.getPuntos() > fichaMayorValor) {
                fichaMayorValor = ficha.getPuntos();
                jugadorQueEmpieza = jugador;
            }else if (ficha.getPuntos() == fichaMayorValor) {
                // Si las fichas tienen el mismo valor, decidir aleatoriamente
                if (Math.random() > 0.5) {
                    jugadorQueEmpieza = jugador;
                }
            }
            bolsa.agregarFicha(ficha);
        }
        turnoActual = jugadorQueEmpieza;
        setTurnoActual(jugadorQueEmpieza);
        notificarObservadores(Eventos.PASAR_TURNO);
        return jugadorQueEmpieza;
    }

    public Jugador getPrimerTurno() throws RemoteException{
        return turnoActual;
    }

    public boolean esTurnoDe(Jugador jugador) throws  RemoteException{
        return turnoActual.equals(jugador);
    }


    public void calcularSiguienteTurno() throws RemoteException {
        turnoActual = jugadores.get((jugadores.indexOf(turnoActual) + 1) % jugadores.size());
    }

    public void pasarTurno() throws RemoteException {
        calcularSiguienteTurno();
        notificarObservadores(Eventos.CAMBIO_ESTADO_PARTIDA);
    }

    public Jugador getJugadorActual() throws RemoteException {
        return this.turnoActual
                ;
    }

    public ArrayList<Jugador> getJugadores() throws RemoteException{
        return jugadores;
    }

    public void setTurnoActual(Jugador turno)throws RemoteException {
        this.turnoActual = turno;
    }

    public Jugador getTurnoActual()throws RemoteException{
        return this.turnoActual;
    }

    public void generarAtril(Jugador jugador) throws RemoteException{
        System.out.println("juego generar");
        jugador.generarAtril(bolsa);
    }

    public ArrayList<Ficha> obtenerFichasDelAtril(Jugador jugador) throws RemoteException{
        return jugador.getAtril();
    }


    public synchronized void conectarJugador(Jugador jugador) throws RemoteException {
        if (!jugadores.contains(jugador)) {
            jugadores.add(jugador);
            jugadoresRegistrados.add(jugador);
            Serializador.guardarJugadores(jugadoresRegistrados);
            notificarObservadores(Eventos.JUGADOR_CONECTADO);
        }
        if (jugadores.size() == cantidadJugadores) {
            notificarObservadores(Eventos.INICIO_PARTIDA);
        }
    }

    public void guardarCoordenadas(PosicionTablero posicion) throws RemoteException{
        coordenadasColocadas.add(posicion);
    }

    public boolean validarPalabra() throws RemoteException{
        boolean validacion = palabra.validarPalabra(coordenadasColocadas);
        return validacion;
    }

    public int calculaPuntaje() throws RemoteException{
        int puntaje = palabra.PuntajePalabra(coordenadasColocadas);
        return puntaje;
    }


    public boolean terminoJuego() throws RemoteException {
        Jugador actual = getJugadorActual();
        if(!actual.getAtril().isEmpty() && !getBolsa().estaVacia()){
            return false;
        }
        if (turnosConsecutivosSinJugar >= jugadores.size() * 2) {
            System.out.println("¡El juego termina! Ningún jugador puede jugar.");
            return false; // Indica que termina el juego
        }
        return true;
    }

    public void colocarFichaEnCelda(String letra, PosicionTablero posicion) throws RemoteException {
        Ficha ficha = new Ficha(letra);
        tablero.obtenerCelda(posicion).setFicha(ficha);
        notificarObservadores(Eventos.CAMBIO_ESTADO_PARTIDA);
    }



    public boolean validarOpcionJugadorMenuPrincipal(int opcion) throws RemoteException {
        // en esta funcion validamos que la opción esté dentro de las opciones válidas del menú principal.
        return opcion == 1 || opcion == 2;
    }

    public Boolean validarCantidadJugadores(int cantidadJugadores) throws RemoteException{
        return cantidadJugadores >= 2 && cantidadJugadores <= 4;
    }

    public Boolean validarPosicionTablero(PosicionTablero posicion)throws RemoteException{
        if(posicion.getPosicionX() < 0 || posicion.getPosicionX() > 14 || posicion.getPosicionY() < 0 || posicion.getPosicionY() >  14){
            return false;
        }
        return true;
    }

    @Override
    public boolean verificarNombreJugador(String nombre) throws RemoteException {
        if(jugadoresRegistrados == null){
            return false;
        }
        for (Jugador jugador : jugadoresRegistrados) {
            if (jugador.getNombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    public boolean celdaLibreYvalida(PosicionTablero posicion) throws RemoteException {
        Celda celda = tablero.getCelda(posicion);  // Obtiene la celda de esa posición
        if (celda.getEstado() == EstadoCelda.LIBRE){
             return true;
         }else{
             return false;
         }
    }

    public Ficha obtenerContenidoCelda(PosicionTablero posicion) throws RemoteException{
        return tablero.obtenerContenidoCelda(posicion);
    }

    public String obtenerIndiceCeldaTablero(PosicionTablero posicion) throws RemoteException{
        return tablero.obtenerBonificacionCelda(posicion);
    }

    public void setCantidadDeJugadores(int cantidadJugadores) throws RemoteException {
        this.cantidadJugadores = cantidadJugadores;
    }

    public void completarAtril() throws RemoteException{
        turnoActual.completarAtril(getBolsa());
    }

}
