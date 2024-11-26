package ar.edu.unlu.Vista;

import ar.edu.unlu.APPCliente;
import ar.edu.unlu.APPServidor;
import ar.edu.unlu.Controlador.ScrabbleControlador;
import ar.edu.unlu.DatosPartidaGuardada;
import ar.edu.unlu.Serializador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class MenuIniciarPartida extends  JFrame {
    private JFrame frame;
    private final ScrabbleControlador controlador;

    public MenuIniciarPartida(ScrabbleControlador controlador) {
        this.controlador = controlador;
        frame = new JFrame("Juego Scrabble Game");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Márgenes

        JButton crearServidorButton = crearBoton("NUEVO SERVIDOR", panelPrincipal);
        JButton unirseServidorButton = crearBoton("UNIRSE A SERVIDOR", panelPrincipal);
        JButton continuarPartidaButton = crearBoton("CONTINUAR PARTIDA", panelPrincipal);

        crearServidorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            //    try {
                    //new APPServidor();
            //    } catch (RemoteException ex) {
             //       throw new RuntimeException(ex);
              //  }
            }
        });

        unirseServidorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new APPCliente();
                dispose();
            }
        });

        // Acción para "Continuar Partida"
        continuarPartidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<DatosPartidaGuardada> partidasGuardadas = Serializador.cargarPartidaGuardada();
                if (partidasGuardadas.isEmpty()) {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No se han encontrado partidas guardadas en el ordenador", "¡AVISO!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    dispose();
                    //new ListaPartidasGuardadas(partidasGuardadas);
                }
            }
        });

        JPanel contenedorPrincipal = new JPanel();
        contenedorPrincipal.setLayout(new BorderLayout());
        contenedorPrincipal.add(panelPrincipal, BorderLayout.CENTER);

        frame.add(contenedorPrincipal);
        frame.setVisible(true);
    }

    private int mostrarCantidadJugadores() {
        String cantidadJugadoresStr = JOptionPane.showInputDialog(frame, "Ingresa la cantidad de jugadores (2-4):");
        int cantidadJugadores = 0;
        try {
            cantidadJugadores = Integer.parseInt(cantidadJugadoresStr);
            if (cantidadJugadores >= 2 && cantidadJugadores <= 4) {
                // Crear servidor con la cantidad de jugadores seleccionados
                JOptionPane.showMessageDialog(frame, "Creando servidor para " + cantidadJugadores + " jugadores.");

            } else {
                JOptionPane.showMessageDialog(frame, "La cantidad de jugadores debe ser entre 2 y 4.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Por favor, ingresa un número válido.");
        }
        return cantidadJugadores;
    }

    private JButton crearBoton(String texto, JPanel panelPrincipal) {
        JButton boton = new JButton(texto);
        boton.setBackground(Color.lightGray);
        boton.setForeground(Color.BLACK);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(Color.white, 2));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(200, 40));
        panelPrincipal.add(boton);
        return boton;
    }
}
