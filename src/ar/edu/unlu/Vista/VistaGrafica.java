package ar.edu.unlu.Vista;

import ar.edu.unlu.Controlador.ScrabbleControlador;
import ar.edu.unlu.Enums.EstadoActual;
import ar.edu.unlu.Enums.Eventos;
import ar.edu.unlu.Modelo.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;


public class VistaGrafica extends JFrame implements IVista{
    private Jugador jugadorLocal;
    private String nombreJugador = "";
    private JPanel panelTablero;
    private JPanel panelAtril;
    private JButton[] botonesAtril;// Array de celdas (tablero unidimensional)
    private JButton[] botonesTablero;
    private ScrabbleControlador controlador;
    private JTextArea textMensajes;
    private Jugador cliente;

    public VistaGrafica(ScrabbleControlador controlador, String nombre) {
        this.nombreJugador = nombre;
        setTitle("Scrabble");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        this.controlador = controlador;
        this.controlador.setVista(this);

        inicializarTablero();
        inicializarAtril();

        textMensajes = new JTextArea(10, 30);
        textMensajes.setEditable(false);
        textMensajes.setLineWrap(true); // Ajustar texto automáticamente a la línea
        JScrollPane scrollPane = new JScrollPane(textMensajes);
        scrollPane.setPreferredSize(new Dimension(200, 0)); // Fijamos ancho del panel
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
       // PanelMensajes.add(scrollPane, BorderLayout.CENTER);

        add(panelTablero, BorderLayout.CENTER);
        add(panelAtril, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.EAST);
    }

    private void inicializarTablero(){
        panelTablero = new JPanel(new GridLayout(15, 15));
        botonesTablero = new JButton[225];
        for (int i = 0; i < 225; i++) {
            final int fila = i / 15;
            final int columna = i % 15;

            JButton botonCelda = new JButton();
            botonCelda.setPreferredSize(new Dimension(40, 40));
            botonCelda.setBackground(Color.LIGHT_GRAY);
            botonCelda.setOpaque(true);
            botonCelda.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            botonesTablero[i] = botonCelda;
            panelTablero.add(botonCelda);
            botonCelda.setEnabled(false);
        }
        add(panelTablero, BorderLayout.CENTER);
    }

    private void inicializarAtril() {
        panelAtril = new JPanel(new BorderLayout()); // Usar BorderLayout para distribuir botones y fichas
        botonesAtril = new JButton[7]; // Máximo 7 fichas por jugador

        // Botones de cambiar ficha y pasar turno
        JButton btnCambiarFicha = new JButton("Cambiar Ficha");
        JButton btnPasarTurno = new JButton("Pasar Turno");
        JButton btnEnviar = new JButton("Enviar");

        // Subpanel para las fichas
        JPanel panelFichas = new JPanel(new FlowLayout());

        // Inicializar las fichas del atril
        for (int i = 0; i < botonesAtril.length; i++) {
            JButton botonFicha = new JButton();
            botonFicha.setPreferredSize(new Dimension(50, 50));
            botonFicha.setEnabled(false); // Inicialmente deshabilitados
            panelFichas.add(botonFicha);
            botonesAtril[i] = botonFicha;

            // Añadir MouseListener para cuando se haga clic en la ficha
            final int indiceFicha = i; // Captura el índice de la ficha en el atril
            botonFicha.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    // Obtiene las coordenadas del tablero donde se hizo clic
                    int fila = -1;
                    int columna = -1;
                    for (int j = 0; j < botonesTablero.length; j++) {
                        if (botonesTablero[j].getBounds().contains(e.getPoint())) {
                            fila = j / 15; // Obtener fila
                            columna = j % 15; // Obtener columna
                            break;
                        }
                    }

                    if (fila != -1 && columna != -1) {
                        // Aquí agregas la ficha al tablero
                        String dato = botonesAtril[indiceFicha].getText(); // Obtienes el texto de la ficha
                        botonesTablero[fila * 15 + columna].setText(dato); // Coloca la ficha en el tablero
                        botonesTablero[fila * 15 + columna].setEnabled(false); // Deshabilita el botón del tablero
                        botonesAtril[indiceFicha].setEnabled(false); // Deshabilita la ficha del atril

                        // Opcional: Actualizar el texto de los mensajes
                        mostrarMensaje("Ficha colocada en: " + fila + ", " + columna);
                    }
                }
            });
        }

        // Agregar los componentes al panel principal
        panelAtril.add(btnCambiarFicha, BorderLayout.WEST);
        panelAtril.add(btnEnviar, BorderLayout.WEST);
        panelAtril.add(panelFichas, BorderLayout.CENTER);
        panelAtril.add(btnPasarTurno, BorderLayout.EAST);

        // Agregar el atril a la interfaz
        add(panelAtril, BorderLayout.SOUTH);
        addListenersBotonesAtril(btnCambiarFicha, btnPasarTurno, btnEnviar);

    }
    private void addListenersBotonesAtril(JButton btnCambiarFicha, JButton btnPasarTurno, JButton btnEnviar) {
        btnCambiarFicha.addActionListener(e -> {
            try {
                if (verificarTurno()) {
                    controlador.cambiarFicha();
                }
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnPasarTurno.addActionListener(e -> {
            try {
                if (verificarTurno()) {
                    controlador.pasarTurno();
                }
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (esTurnoActual()) {
                    try {
                        boolean valida = controlador.validarPalabra();
                        if(valida){
                            controlador.enviarPalabraYCalcularPuntaje();
                        }
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void setJugadorLocal(Jugador jugador) {
        this.jugadorLocal = jugador;
    }

    public Jugador getJugadorLocal() {
        return jugadorLocal;
    }

    @Override
    public boolean esTurnoActual() {
        return controlador.obtenerTurnoActual().equals(nombreJugador);
    }


    public ArrayList<Integer> obtenerIndicesFichasSeleccionadas() {
        ArrayList<Integer> indicesSeleccionados = new ArrayList<>();
        for (int i = 0; i < botonesAtril.length; i++) {
            if (botonesAtril[i].getBackground().equals(Color.YELLOW)) {
                indicesSeleccionados.add(i);
            }
        }
        return indicesSeleccionados;
    }

    public void fichaSeleccionadaPorElJugador(int indice) {
        // Lógica para seleccionar la ficha del jugador
        println("Ficha seleccionada en el índice: " + indice);
    }



    @Override
    public void habilitarSeleccionFicha() {
        for (int i = 0; i < botonesAtril.length; i++) {
            final int indice = i;
            limpiarListeners(botonesAtril[i]);

            botonesAtril[i].addActionListener(e -> {
                if (botonesAtril[indice].getBackground().equals(Color.YELLOW)) {
                    botonesAtril[indice].setBackground(null);
                } else {
                    botonesAtril[indice].setBackground(Color.YELLOW);
                }
                try {
                    controlador.fichaSeleccionadaPorElJugador(indice);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            });

            botonesAtril[i].setEnabled(true);
            botonesAtril[i].setBackground(Color.YELLOW);
            botonesAtril[i].setText("Ficha " + (i + 1));
        }
    }


    private void limpiarListeners(JButton boton) {
        for (ActionListener al : boton.getActionListeners()) {
            boton.removeActionListener(al);
        }
    }


    @Override
    public void habilitarSeleccionCelda(Ficha fichaSeleccionada) {
        for (int i = 0; i < botonesTablero.length; i++) {
            final int fila = i / 15;       // Calcular fila desde índice lineal
            final int columna = i % 15;    // Calcular columna desde índice lineal
            final PosicionTablero posicion = new PosicionTablero(fila, columna);

            JButton boton = botonesTablero[i];

            // Remover ActionListeners anteriores (para evitar duplicados)
            for (ActionListener al : boton.getActionListeners()) {
                boton.removeActionListener(al);
            }

            try {
                // Verificar si la celda está libre y válida antes de habilitar
                boolean esCeldaValida = controlador.esCeldaLibreYvalida(posicion);

                if (esCeldaValida) {
                    boton.setEnabled(true);
                    //boton.setBackground(Color.lightGray); // Resaltar como válida

                    // Añadir un listener para colocar la ficha seleccionada
                    boton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            controlador.colocarFichaSeleccionadaEnLaCeldaSeleccionada(fichaSeleccionada, posicion);
                            try {
                                controlador.guardarCoordenadas(posicion);
                            } catch (RemoteException ex) {
                                throw new RuntimeException(ex);
                            }
                            //deshabilitarSeleccionCeldaYFicha(); // Deshabilitar todo después de colocar
                        }
                    });
                }  else {
                    boton.setEnabled(false);
                    boton.setBackground(Color.RED); // Resaltar como inválida
                }
            } catch (RemoteException ex) {
                throw new RuntimeException("Error al verificar la celda", ex);
            }
        }
    }

    public void habilitarAtril() {
        for (int i = 0; i < botonesAtril.length; i++) {
            final int indice = i;
            limpiarListeners(botonesAtril[i]);

            botonesAtril[i].addActionListener(e -> {
                try {
                    controlador.fichaSeleccionadaPorElJugador(indice);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            });

            botonesAtril[i].setEnabled(true);
            botonesAtril[i].setBackground(Color.YELLOW);
        }
    }

    public void deshabilitarAtril() {
            for (int i = 0; i < botonesAtril.length; i++) {
                botonesAtril[i].setEnabled(false);  // Deshabilitar el botón
                botonesAtril[i].setBackground(null);  // Restaurar color original
                for (ActionListener al : botonesAtril[i].getActionListeners()) {
                    botonesAtril[i].removeActionListener(al);  // Limpiar listeners
                }
            }
    }



    public void deshabilitarSeleccionCeldaYFicha() {
        // Deshabilitar y limpiar listeners de los botones del tablero
        for (int i = 0; i < botonesTablero.length; i++) {
            for (ActionListener al : botonesTablero[i].getActionListeners()) {
                botonesTablero[i].removeActionListener(al);
            }
            botonesTablero[i].setEnabled(false);
        }

        // Deshabilitar y limpiar listeners de los botones del atril
        for (JButton jButton : botonesAtril) {
            for (ActionListener al : jButton.getActionListeners()) {
                jButton.removeActionListener(al);
            }
            jButton.setEnabled(false);
        }
    }

    public void mostrarTablero(ArrayList<Celda> celdas) {
        panelTablero.removeAll();  // Limpiar el panel para actualizar el tablero

        for (int i = 0; i < celdas.size(); i++) {
            Celda celda = celdas.get(i);
            JButton boton = botonesTablero[i];
            boton.setEnabled(true);
            if (celda.getFicha() != null) {
                boton.setText(String.valueOf(celda.getFicha().getLetra()));
                boton.setBackground(Color.WHITE);
            } else {
                switch (celda.celdaBonificacion()) {
                    case "DP":
                        boton.setText("2W");
                        boton.setBackground(Color.YELLOW);
                        break;
                    case "TP":
                        boton.setText("3W");
                        boton.setBackground(Color.magenta);
                        break;
                    case "DL":
                        boton.setText("2L");
                        boton.setBackground(Color.CYAN);
                        break;
                    case "TL":
                        boton.setText("3L");
                        boton.setBackground(Color.blue.brighter());
                        break;
                    default:
                        boton.setText("");
                        boton.setBackground(Color.GREEN);
                        break;
                }
            }
            boton.setOpaque(true);
            panelTablero.add(boton);
        }
        panelTablero.revalidate();  // Asegura que los cambios en el panel sean reflejados
        panelTablero.repaint();     // Redibuja el panel con los nuevos cambios
    }


    @Override
    public void actualizarVista(EstadoActual estado) {

    }

    @Override
    public void mostrarTurnoJugador(Jugador jugador) {

    }

    public void mostrarMensaje(String texto){
        println(texto);

    }

    public void actualizarTablero() throws RemoteException {

        ArrayList<Celda> celdas = controlador.obtenerTablero();
        mostrarTablero(celdas);
    }


    private void print(String texto) {
        textMensajes.append(texto);
    }

    private void println(String texto) {
        textMensajes.append("\n" + texto + "\n");
    }
    @Override
    public void mostrarMenuTurno(){
    }

    public void mostrarAtril() throws RemoteException {
        Jugador jugador = controlador.jugadorTurnoActual();
        ArrayList<Ficha> atril = jugador.getAtril();

        for (int i = 0; i < botonesAtril.length; i++) {
            if (i < atril.size() && atril.get(i) != null) {
                botonesAtril[i].setText(atril.get(i).getLetra()); // Muestra la letra de la ficha
                botonesAtril[i].setEnabled(true); // Habilita el botón
            }
        }
    }

    public void actualizarAtril(ArrayList<Ficha> fichas) throws RemoteException {
        for (int i = 0; i < fichas.size(); i++) {
            Ficha ficha = fichas.get(i);
            botonesAtril[i].setText(ficha.getLetra()); // Mostrar la letra en el botón
            botonesAtril[i].setEnabled(true);         // Habilitar el botón
        }
    }

    public void mostrarMensajeErrorCelda(){
        println("la celda seleccionada ya esta ocupada");
    }

    public void iniciarVista() throws RemoteException {
        setVisible(true);
        this.cliente = controlador.registrarJugador(nombreJugador);
        //setVisible(true);
        //controlador.iniciarJuego();
    }


    public void actualizarTableroyAtril(Jugador jugador,ArrayList<Ficha> ficha, Eventos evento) throws RemoteException {
        if (evento == Eventos.CAMBIO_FICHA){
            actualizarAtril(ficha);
        }else{
            actualizarTablero();
            actualizarAtril(ficha);
        }

    }

    @Override
    public void mostrarJugadorConectado() throws RemoteException {
        setVisible(true);
        textMensajes.setText("Te has conectado.\nEsperando a que se conecte tu oponente...");
    }

    @Override
    public boolean verificarTurno() throws RemoteException {
        if(!controlador.obtenerTurnoActual().equals(nombreJugador)){
            println("\nEs turno del: "+ controlador.obtenerTurnoActual());
        }
        else{
            println("\nES TU TURNO "+ controlador.obtenerTurnoActual());
            //mostrarMenuTurno();
        }

        return false;
    }
}


   /* @Override
    public void actualizarVista(Eventos evento) {
        switch (evento) {
            case ESPERANDO_TURNO ->
                    println(textMensajes, "\nAhora es el turno de tu oponente.\nEspera hasta que realice su movimiento ;)");
            case PASAR_TURNO -> {
                manejarCambioDeTurno(juego);
            }
            case CAMBIO_FICHA -> {

            }
                limpiarMensajes();
                mostrarMensajeEsTuTurno();
                mensajeCasillaFichaAMover();
            }
            case SELECCIONAR_DESTINO_MOVER -> {
                limpiarMensajes();
                mostrarMensajeEsTuTurno();
                mensajePedirNuevaCasillaLibreAdyacente();
            }
            case SELECCIONAR_FICHA_PARA_ELIMINAR -> {
                mostrarMensajeEsTuTurno();
                mensajeFichaAEliminar();
            }
            case PARTIDA_SUSPENDIDA -> {

    }*/

