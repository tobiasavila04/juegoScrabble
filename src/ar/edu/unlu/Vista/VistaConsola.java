package ar.edu.unlu.Vista;

import ar.edu.unlu.Controlador.ScrabbleControlador;
import ar.edu.unlu.Enums.EstadoActual;
import ar.edu.unlu.Enums.Eventos;
import ar.edu.unlu.Modelo.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Locale;

public class VistaConsola extends JFrame implements IVista {
    private ScrabbleControlador controlador;
    private Jugador jugador;
    private String nombreJugador = "";
    private JTextArea textMensajes;
    private JTextArea textTablero;
    private final JTextField inputFila;
    private final JTextField inputColumna;
    private final JTextField inputLetra;
    private JTextField inputEntrada;
    private JButton enter;
    private Estado estado;

    public VistaConsola(ScrabbleControlador controlador, String nombre) throws RemoteException {
        this.nombreJugador =nombre;
        this.controlador = controlador;
        controlador.setVista(this);
        // Configuración de la ventana
        setTitle("Scrabble - Vista Consola");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLayout(new BorderLayout(10, 10));

        // Panel izquierdo: Tablero y atril
        textTablero = new JTextArea();
        textTablero.setEditable(false);
        textTablero.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollTablero = new JScrollPane(textTablero);
        add(scrollTablero, BorderLayout.CENTER);

        // Panel derecho: Mensajes
        textMensajes = new JTextArea();
        textMensajes.setEditable(false);
        JScrollPane scrollMensajes = new JScrollPane(textMensajes);
        scrollMensajes.setPreferredSize(new Dimension(250, getHeight()));
        add(scrollMensajes, BorderLayout.EAST);

        // Panel inferior: Entrada de datos y botón de acción
        JPanel panelInferior = new JPanel(new FlowLayout());
        JLabel labelFila = new JLabel("Fila:");
        inputFila = new JTextField(2);
        JLabel labelColumna = new JLabel("Columna:");
        inputColumna = new JTextField(2);
        JLabel labelLetra = new JLabel("Letra:");
        inputLetra = new JTextField(2);


        JButton botonEnviar = new JButton("Colocar Ficha");
        botonEnviar.addActionListener(e -> {
            try {
                String fila = inputFila.getText().trim();
                String columna = inputColumna.getText().trim();
                String letra = inputLetra.getText().trim();
                if (fila.isEmpty() || columna.isEmpty() || letra.isEmpty()) {
                    mostrarMensaje("Por favor, completa todos los campos.");
                } else {
                    if (controlador.letraEnAtril(letra.toUpperCase())) {
                        // Agregar la letra al tablero en la posición indicada
                        controlador.agregarLetraAlTablero(fila, columna, letra);
                        actualizarTablero();
                        PosicionTablero posicion = new PosicionTablero(Integer.parseInt(fila) - 1, columna.charAt(0) - 'A');
                        controlador.guardarCoordenadas(posicion);
                    } else {
                        mostrarMensaje("La letra no está en tu atril.");
                       // limpiarPantalla();;
                    }
                    mostrarAtril();
                    actualizarTablero();
                  //  limpiarPantalla();
                }
            } catch (RemoteException ex) {
                mostrarMensaje("Error al colocar ficha: " + ex.getMessage());
            }
        });

        JButton botonEnviarPalabra = new JButton("Enviar Palabra");
        botonEnviarPalabra.addActionListener(e -> {
            try {
                // Validar la palabra con las coordenadas almacenadas
                boolean palabraValida = controlador.validarPalabra();
                if (palabraValida) {
                    mostrarMensaje("Palabra válida y agregada al tablero.");
                    int puntaje = controlador.enviarPalabraYCalcularPuntaje();
                    mostrarMensaje("Puntaje obtenido: " + puntaje);
                    //coordenadasPalabra.clear(); // Limpiar las coordenadas después de validar la palabra
                } else {
                    mostrarMensaje("La palabra no es válida.");
                }
            } catch (RemoteException ex) {
                mostrarMensaje("Error al enviar la palabra: " + ex.getMessage());
            }
        });


        JTextField campoEntradaMensaje = new JTextField(30);
        enter = new JButton("Enter");
        enter.addActionListener( e -> {
            String mensaje = campoEntradaMensaje.getText().trim();
            if (!mensaje.isEmpty()) {
                // Procesar el mensaje y agregarlo al área de mensajes
                println(mensaje);
                procesarEntrada(mensaje);
                campoEntradaMensaje.setText("");  // Limpiar el campo de entrada después de enviarlo
            }
        });

        panelInferior.add(labelFila);
        panelInferior.add(inputFila);
        panelInferior.add(labelColumna);
        panelInferior.add(inputColumna);
        panelInferior.add(labelLetra);
        panelInferior.add(inputLetra);
        panelInferior.add(botonEnviar);
        panelInferior.add(botonEnviarPalabra);
        panelInferior.add(enter);
        panelInferior.add(campoEntradaMensaje);

        add(panelInferior, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }



    private void procesarEntrada(String entrada) {
        switch (estado) {
            case Menu_turno:
                //limpiarPantalla();
                procesarEntradaMenu(entrada);
                break;

        }
    }


    private void print(String string) {
        textMensajes.append(string);
    }

    private void println(String texto) {
        textMensajes.append(texto + "\n");
    }



    @Override
    public void mostrarTablero(ArrayList<Celda> celdas) throws RemoteException {
        StringBuilder tablero = new StringBuilder();
        String contenido;
        tablero.append("      A   B   C   D   E   F   G   H   I   J   K   L   M   N   O\n");
        for (int fila = 0; fila < 15; fila++) {
            tablero.append(String.format(" " + "%2d", fila + 1));
            for (int columna = 0; columna < 15; columna++) {
                int indice = fila * 15 + columna; // Cálculo del índice correcto
                Celda celda = celdas.get(indice); // Obtén la celda correcta
                contenido = celda.celdaBonificacion(); // Extrae el contenido
                tablero.append(" " + contenido + " ");
            }
            tablero.append(String.format(" %2d\n", fila + 1)); // Cierra la fila
        }
        tablero.append("      A   B   C   D   E   F   G   H   I   J   K   L   M   N   O\n");
        textTablero.setText(tablero.toString());
    }


    @Override
    public void actualizarVista(EstadoActual estado) {

    }

    @Override
    public void mostrarTurnoJugador(Jugador jugador) {
        println("Turno de: " + jugador.getNombre());
    }


    @Override
    public void mostrarAtril() throws RemoteException {
        StringBuilder atril = new StringBuilder();
        Jugador jugador = controlador.jugadorTurnoActual();
        for (Ficha ficha : jugador.getAtril()) {
            String letra = String.valueOf(ficha.getLetra());
            atril.append("│ ").append(letra).append(" │ ");
        }

        // Mostramos el atril en el área de mensajes
        textTablero.append(atril.toString());
    }



    @Override
    public void habilitarSeleccionFicha() {

    }

    @Override
    public void habilitarSeleccionCelda(Ficha ficha) throws RemoteException {

    }


    @Override
    public void deshabilitarSeleccionCeldaYFicha() {

    }

    @Override
    public void actualizarTablero() throws RemoteException {
        ArrayList<Celda> celdas = controlador.obtenerTablero();
        mostrarTablero(celdas);

    }



    public boolean verificarTurno() throws RemoteException {
        if(!controlador.obtenerTurnoActual().equals(nombreJugador)){
            println("\nEs turno del: "+ controlador.obtenerTurnoActual());
        }
        else{
            println("\nES TU TURNO "+ controlador.obtenerTurnoActual());
            mostrarMenuTurno();
        }
        return false;
    }

    @Override
    public void iniciarVista() throws IOException {
        setVisible(true);
        this.jugador = controlador.registrarJugador(nombreJugador);
    }

    @Override
    public void actualizarTableroyAtril(Jugador jugador, ArrayList<Ficha> ficha, Eventos evento) throws RemoteException {

    }

    @Override
    public void actualizarAtril(ArrayList<Ficha> ficha) throws RemoteException {

    }

    @Override
    public void mostrarMenuTurno() {
        estado = Estado.Menu_turno;
       // textMensajes.setEditable(true);
        mostrarMensaje("¿Seleccione una opcion: \n");
        mostrarMensaje("1. Agregar palabra.\n");
        mostrarMensaje("2. Cambiar fichas.\n");
        mostrarMensaje("3. Pasar turno.\n");
        mostrarMensaje("Seleccione una opción: ");
    }

    private void procesarEntradaMenu(String entrada) {
        switch (entrada) {
            case "1":
                mostrarMensaje("inrgese coordenadas y una vez colocadas todas las fichas envie la palabra\n");
            case "2":
                controlador.cambiarFicha();
            case "3":
                controlador.pasarTurno();
            default:
                print("Opcion no valida, elija una opcion valida: ");
                mostrarMenuTurno();
        }
    }


    @Override
    public void mostrarMensaje(String s) {
        println(s + "\n");

    }

    @Override
    public void mostrarJugadorConectado() throws RemoteException {

    }

    @Override
    public ArrayList<Integer> obtenerIndicesFichasSeleccionadas() {
        return null;
    }

    @Override
    public void setJugadorLocal(Jugador jugador) {

    }

    @Override
    public Jugador getJugadorLocal() {
        return null;
    }

    @Override
    public void deshabilitarAtril() {

    }

    @Override
    public void habilitarAtril() {

    }

    @Override
    public boolean esTurnoActual() {
        return false;
    }

    private PosicionTablero generarCoordenada(String fila, String columna) {
        // Validación básica de los valores ingresados
        if (fila == null || columna == null || fila.isEmpty() || columna.isEmpty()) {
            throw new IllegalArgumentException("Fila y columna no pueden estar vacías");
        }

        // Convierte la fila a entero
        int filaNumerica;
        try {
            filaNumerica = Integer.parseInt(fila);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La fila debe ser un número entre 0 y 14");
        }

        // Convierte la columna de letra a índice numérico
        char columnaChar = columna.toUpperCase().charAt(0);
        if (columnaChar < 'A' || columnaChar > 'O') {
            throw new IllegalArgumentException("La columna debe ser una letra entre A y O");
        }
        int columnaNumerica = columnaChar - 'A'; // Convierte A = 0, B = 1, ..., O = 14

        // Verifica que las coordenadas estén dentro de los límites del tablero
        if (filaNumerica < 0 || filaNumerica >= 15 || columnaNumerica < 0 || columnaNumerica >= 15) {
            throw new IllegalArgumentException("La fila y la columna deben estar dentro del rango del tablero (0-14 para filas, A-O para columnas)");
        }

        // Retorna un nuevo objeto Coordenada
        return new PosicionTablero(filaNumerica, columnaNumerica);
    }

    public void limpiarPantalla() {
        // Limpiar el área de mensajes
        textMensajes.setText("");
    }
}
