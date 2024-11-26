package ar.edu.unlu.Controlador;

import ar.edu.unlu.Enums.Eventos;
import ar.edu.unlu.Modelo.*;
import ar.edu.unlu.Vista.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ScrabbleControlador implements IControladorRemoto {
    IVista vista;
    IScrabbleGame juego;
    //private IScrabbleGame modelo;


    public ScrabbleControlador() {}


    public void setVista(IVista vista) {
        this.vista = vista;
    }

    public void iniciarJuego() {
        try {
            juego.inicializarJuego();
            vista.mostrarMensaje("¡El juego ha comenzado!");
            juego.inicializarPrimerTurno(juego.getBolsa());
            vista.mostrarTablero(juego.getTablero().getCeldas());
         //   Jugador jugador = juego.getTurnoActual();
            vista.mostrarAtril();
            if(vista.esTurnoActual()){
                vista.mostrarMenuTurno();
            }
        } catch (RemoteException e) {
            manejarError(e, "Error al iniciar el juego.");
        }
    }

    public void pasarTurno() {
        if(vista.esTurnoActual()) {
            try {
                juego.pasarTurno();
            } catch (RemoteException e) {
                // TODO Bloque catch generado automáticamente
                e.printStackTrace();
            }
        }
    }

    public void cambiarFicha() {
        if(vista.esTurnoActual()){
            try {
                ArrayList<Integer> indicesFichasACambiar = vista.obtenerIndicesFichasSeleccionadas();

                if (indicesFichasACambiar.isEmpty()) {
                    vista.mostrarMensaje("No se seleccionaron fichas para cambiar.");
                    return;
                }

                ArrayList<Ficha> nuevasFichas = juego.cambiarFichas(
                        vista.getJugadorLocal().getId(),
                        indicesFichasACambiar
                );
                vista.actualizarAtril(nuevasFichas);
            } catch (RemoteException e) {
                manejarError(e, "Error al cambiar fichas.");
            }
        }
    }

    private void actualizarTableroYJugadores() {
        try {
            vista.mostrarTablero(juego.getTablero().getCeldas());
            vista.mostrarTurnoJugador(juego.getJugadorActual());
        } catch (RemoteException e) {
            manejarError(e, "Error al actualizar el tablero y jugadores.");
        }
    }

    @Override
    public void actualizar(IObservableRemoto instanciaModelo, Object cambio) throws RemoteException {
        if (cambio instanceof Eventos) {
            switch ((Eventos) cambio) {
                case INICIO_PARTIDA -> {
                    iniciarJuego();
                    juego.inicializarPrimerTurno(juego.getBolsa());
                    vista.verificarTurno();
                    break;
                }
                case PASAR_TURNO -> vista.verificarTurno();
                case CAMBIO_ESTADO_PARTIDA -> {
                    actualizarEstadoTurno();
                    vista.actualizarTablero();
                    actualizarAtril();
                }
                case Actualizar_tablero -> vista.mostrarTablero(juego.getTablero().getCeldas());
                case FIN_JUEGO -> vista.mostrarMensaje("¡El juego ha terminado!");
                case JUGADOR_CONECTADO -> vista.mostrarMensaje("Esperando a que se conecte con tu oponente...\n");
                default -> vista.mostrarMensaje("Evento no manejado: " + cambio);
            }
        }
    }

    private void actualizarEstadoTurno() {
        try {
            Jugador turnoActual = juego.getTurnoActual();
            Jugador jugadorLocal = vista.getJugadorLocal();

            if (turnoActual.equals(jugadorLocal)) {
                vista.mostrarMensaje("Es tu turno, " + jugadorLocal.getNombre() + "!");
                vista.habilitarAtril();
            } else {
                vista.mostrarMensaje("Es el turno de " + turnoActual.getNombre() + ".");
                vista.deshabilitarAtril();
            }
        } catch (RemoteException e) {
            manejarError(e, "Error al actualizar el estado del turno.");
        }
    }


    public void realizarAccionSiEsTurno(Runnable accion) {
        try {
            Jugador turnoActual = juego.getTurnoActual();
            if (turnoActual.equals(vista.getJugadorLocal())) {
                accion.run(); // Ejecuta la acción solo si es el turno del jugador local.
            } else {
                vista.mostrarMensaje("No es tu turno.");
            }
        } catch (RemoteException e) {
            manejarError(e, "Error al validar turno.");
        }
    }

    private void manejarError(Exception e, String mensaje) {
        vista.mostrarMensaje(mensaje);
        e.printStackTrace();
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.juego = (IScrabbleGame) modeloRemoto;

    }

    public boolean esCeldaLibreYvalida(PosicionTablero posicion) throws RemoteException {
        if (!juego.celdaLibreYvalida(posicion)) {
            vista.mostrarMensaje(" celda no valida");
            return false;
        }
        return true;
    }

    public void colocarFichaSeleccionadaEnLaCeldaSeleccionada(Ficha ficha, PosicionTablero posicion) {
        realizarAccionSiEsTurno(() -> {
            try {
                if (juego.celdaLibreYvalida(posicion)) {
                    juego.colocarFichaEnCelda(ficha.getLetra(), posicion);
                    guardarCoordenadas(posicion);
                    vista.mostrarMensaje("Ficha colocada.");
                    actualizarTableroYJugadores();
                }
            } catch (RemoteException e) {
                manejarError(e, "Error al colocar ficha.");
            }
        });
    }

    public void fichaSeleccionadaPorElJugador(int indice) throws RemoteException{
        try {
            // Obtener la ficha del atril en base al índice
            Ficha fichaSeleccionada = juego.getTurnoActual().getAtril().get(indice);
            // Habilitar la selección de celdas para colocar la ficha
            vista.habilitarSeleccionCelda(fichaSeleccionada);  // Pasar la ficha seleccionada a la vista
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Celda> obtenerTablero() throws RemoteException {
        return juego.getTablero().getCeldas();  // Devuelve el estado actual del tablero
    }

    public Jugador registrarJugador(String nombre) throws RemoteException {
        Jugador jugador = new Jugador(nombre);
        juego.conectarJugador(jugador);
        vista.setJugadorLocal(jugador);
        return jugador;
    }



    public String obtenerTurnoActual() {
        String nombre=null;
        try {
            Jugador jugadorActual = juego.getJugadorActual();
            nombre=jugadorActual.getNombre();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return nombre;
    }

    public int enviarPalabraYCalcularPuntaje() throws RemoteException {
        boolean palabraValida = juego.validarPalabra();
        int puntaje = juego.calculaPuntaje();
        actualizarPuntajeJugador(puntaje);
        return puntaje;

    }

    public boolean validarPalabra() throws RemoteException {
        return juego.validarPalabra();
    }

    public void actualizarPuntajeJugador(int puntaje) throws RemoteException {
        Jugador jugadorActual = juego.getJugadorActual();
        jugadorActual.agregarPuntos(puntaje);
    }

    public void guardarCoordenadas(PosicionTablero posicion) throws RemoteException {
        juego.guardarCoordenadas(posicion);
        vista.mostrarMensaje("coordenas: " + posicion);

    }

    public String obtenerContenidoCelda(PosicionTablero posicion) throws RemoteException {
        return juego.obtenerIndiceCeldaTablero(posicion);
    }

    public Jugador jugadorTurnoActual() throws RemoteException {
        return juego.getTurnoActual();
    }

    public boolean letraEnAtril(String letra) throws RemoteException {
        // Recorremos el atril del jugador buscando si tiene la letra disponible
        Jugador jugador = juego.getJugadorActual();
        ArrayList<Ficha> fichas = jugador.getAtril();
        for (int i = 0; i < fichas.size(); i++) {
            Ficha ficha = fichas.get(i);
            if (ficha.getLetra().equals(letra)) {
                return true; // La letra está en el atril
            }
        }
        return false; // La letra no está en el atril
    }

    public void agregarLetraAlTablero(String fila, String columna, String letra) throws RemoteException {
        int filaIndex = Integer.parseInt(fila) - 1;
        int columnaIndex = columna.charAt(0) - 'A'; // Convertir la letra de la columna a índice
        PosicionTablero posicion = new PosicionTablero(filaIndex,columnaIndex);

        // Obtener la celda correspondiente
        Celda celda = juego.getTablero().getCelda(posicion);
        // Verificar si la celda está vacía (si tiene contenido o no)
        if (celda.getFicha() == null) {
            eliminarLetraDelAtril(letra);
            juego.colocarFichaEnCelda(letra, posicion);
        } else {
            vista.mostrarMensaje("La celda ya está ocupada.");
        }
    }

    // Método para eliminar la letra del atril del jugador
    private void eliminarLetraDelAtril(String letra) throws RemoteException {
        Jugador jugador = juego.getJugadorActual();
        for (int i = 0; i < jugador.getAtril().size(); i++) {
            Ficha ficha = jugador.getAtril().get(i);
            if (ficha.getLetra().equals(letra)) {
                // Eliminamos la ficha del atril
                jugador.getAtril().remove(i);
                break; // Salimos del bucle después de encontrar y eliminar la letra
            }
        }
    }
    public void actualizarAtril() throws RemoteException {
        Jugador jugador = juego.getJugadorActual();
        juego.completarAtril();
        vista.actualizarAtril(juego.obtenerFichasDelAtril(jugador));
    }











    /*public void iniciarJuego() throws RemoteException {
        try {
            juego.inicializarJuego();
           // cambiarEstado(EstadoActual.INICIALIZAR_JUEGO);

            vista.mostrarTablero(juego.getTablero().getCeldas());
            vista.actualizarTablero();
            //juego.setTurnoActual(juego.getPrimerTurno());
            int cantJugadores = juego.getCantidadDeJugadores();

            for(int i = 0; i < cantJugadores; i++){
                System.out.println(cantJugadores);
                System.out.println("jugadorrrrr " + i);
                Jugador jugador = juego.getJugadores().get(i);
                if (jugador.getAtril().getFicha().isEmpty()) {  // Verifica si el atril está vacío
                    juego.generarAtril(jugador);  // Genera

                    // el atril solo si está vacío
                }
                vista.actualizarAtril(jugador);
            }
            //cambiarEstado(EstadoActual.TURNO_JUGADOR);
            juego.inicializarPrimerTurno(juego.getBolsa());

            while (!juego.terminoJuego()) {
                Jugador jugadorActual = juego.getJugadorActual();
                vista.mostrarAtril(jugadorActual);
                cambiarEstado(EstadoActual.MENU_TURNO);

                manejarCambioDeTurno();

                //vista.mostrarBotones(jugadorActual);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cambiarFicha() throws RemoteException {
        Ficha sacarFicha = juego.getBolsa().sacarFichaDeLaBolsa();
        vista.mostrarFichaSacadaDeLaBolsa(sacarFicha.getLetra());
        if(juego != null){
            juego.getTurnoActual().getAtril().agregarFichaAtril(sacarFicha);
        }
        assert juego != null;
        vista.actualizarTableroyAtril(juego.getJugadorActual(),juego.getJugadorActual().getAtril().getFicha(),Eventos.CAMBIO_FICHA);
        Jugador turno = juego.calcularSiguienteTurno();
        juego.setTurnoActual(turno);
    }

    public void colocarFicha() throws RemoteException {
        vista.mostrarTablero(juego.getTablero().getCeldas());
        Atril atril = juego.getTurnoActual().getAtril();
        vista.mostrarAtril(juego.getJugadorActual());
        vista.habilitarSeleccionFicha();  // Permite seleccionar fichas del atril
    }

    public void pasarTurno() throws RemoteException {
        Jugador turno = juego.calcularSiguienteTurno();
        juego.setTurnoActual(turno);
        vista.actualizarTableroyAtril(juego.getJugadorActual() ,juego.getJugadorActual().getAtril().getFicha(), Eventos.PASAR_TURNO);
    }



    public void fichaSeleccionadaPorElJugador(int indice) throws RemoteException{
        try {
            // Obtener la ficha del atril en base al índice
            Ficha fichaSeleccionada = juego.getTurnoActual().getAtril().getFicha().get(indice);
            // Habilitar la selección de celdas para colocar la ficha
            vista.habilitarSeleccionCelda(fichaSeleccionada);  // Pasar la ficha seleccionada a la vista
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void colocarFichaSeleccionadaEnLaCeldaSeleccionada(Ficha fichaSeleccionada, PosicionTablero posicion) throws RemoteException{
        try {
            if (esCeldaLibreYvalida(posicion)) {
                juego.colocarFichaEnCelda(fichaSeleccionada, posicion);
                vista.actualizarTablero();
                vista.deshabilitarSeleccionCeldaYFicha();
                Jugador turno = juego.calcularSiguienteTurno();
                juego.setTurnoActual(turno);
                vista.mostrarTurnoJugador(turno);
            } else {
                vista.mostrarMensajeErrorCelda();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void manejarCambioDeTurno() {
        try {
            Jugador jugadorActual = juego.getTurnoActual();
            for (Jugador jugador : juego.getJugadores()) {
                if (jugador.equals(jugadorActual)) {
                    vista.mostrarMensaje("Es tu turno, " + jugador.getNombre() + ".");
                } else {
                    vista.mostrarMensaje("Es el turno de " + jugadorActual.getNombre() + ".");
                }
            }
        } catch (RemoteException e) {
            vista.mostrarMensaje("Error al obtener el turno actual.");
            e.printStackTrace();
        }
    }

    public Jugador registrarJugador(String nombre) throws RemoteException {
        Jugador jugador = new Jugador(nombre);
        juego.conectarJugador(jugador);
        return jugador;
    }



    public ArrayList<Ficha> obtenerFichasAtril(Jugador jugador) throws RemoteException {
        return juego.obtenerFichasDelAtril(jugador);
    }

    private void cambiarEstado(EstadoActual estado) {
        vista.actualizarVista(estado);
    }

    public Boolean validarOpcionFichaAtril(int opcion, Atril atril) {
        return opcion >= 1 && opcion <= atril.getFicha().size();
    }

    public boolean esCeldaLibreYvalida(PosicionTablero posicion) throws RemoteException {
        if (!juego.celdaLibreYvalida(posicion)) {
            vista.mostrarMensajeErrorCelda();
            return false;
        }
        return true;
    }


    public Ficha valorDeLaCelda(PosicionTablero posicion) throws RemoteException {
        return juego.obtenerContenidoCelda(posicion);
    }

    public Celda indiceCelda(int indice) throws RemoteException {
        return juego.obtenerIndiceCeldaTablero(indice);
    }

    public boolean NombreYaRegistrado(String nombre) throws RemoteException {
        return juego.verificarNombreJugador(nombre);

    }

    public ArrayList<Jugador> getJugadores() throws RemoteException{
        ArrayList<Jugador> jugadores = juego.getJugadores();
        return jugadores;
    }


    public ArrayList<Celda> obtenerTablero() throws RemoteException {
        return juego.getTablero().getCeldas();  // Devuelve el estado actual del tablero
    }

    public Jugador obtenerJugadores(int idJugador){
        try {
            return juego.getJugadores().get(idJugador);
        } catch (RemoteException e) {
            return null;
        }
    }

    public Jugador obtenerTurnoActual() {
        try {
            return juego.getTurnoActual();
        } catch (RemoteException e) {
            // TODO Bloque catch generado automáticamente
            return null;
        }
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.juego = (IScrabbleGame) modeloRemoto;

    }

    @Override
    public void actualizar(IObservableRemoto instanciaModelo, Object cambio) throws RemoteException {
        if (cambio instanceof Eventos) {
            switch ((Eventos) cambio) {
                case INICIO_PARTIDA:
                    vista.mostrarMensaje("¡La partida ha comenzado!");
                    iniciarJuego();
                    break;
                case PASAR_TURNO:
                    manejarCambioDeTurno();
                    break;
                case Actualizar_tablero:
                    vista.actualizarTablero();
                    break;
                case CAMBIO_FICHA:
                    vista.mostrarFichaSacadaDeLaBolsa(juego.getBolsa().toString());
                    break;
                case FIN_JUEGO:
                    vista.mostrarMensaje("¡El juego ha terminado!");
                    break;
                case JUGADOR_CONECTADO:
                    vista.mostrarJugadorConectado();
                    break;
                default:
                    break;
            }
        }

    }

    public void cantJugadoresPartida(int cantidadJugadores) throws RemoteException {
        juego.setCantidadDeJugadores(cantidadJugadores);
    }

    public Jugador desconectarJugador(Jugador jugador) {
        try {
            return juego.desconectarJugador(jugador);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }*/
}
