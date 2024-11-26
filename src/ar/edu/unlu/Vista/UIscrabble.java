package ar.edu.unlu.Vista;

import ar.edu.unlu.APPCliente;
import ar.edu.unlu.APPServidor;
import ar.edu.unlu.Controlador.ScrabbleControlador;
import ar.edu.unlu.DatosPartidaGuardada;
import ar.edu.unlu.Modelo.Scrabblegame;
import ar.edu.unlu.Serializador;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;

public class UIscrabble extends JFrame {
    private JFrame frame;
    private JPanel panel1;


    public UIscrabble(ScrabbleControlador controlador) {
        frame = new JFrame("Juego Scrabble Game");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Márgenes alrededor del panel

        JLabel etiqueta = new JLabel("<html>Scrabble es un juego de palabras clásico y divertido. " +
                "El objetivo del juego es obtener la mayor cantidad de puntos al formar palabras en un tablero que se conecten a las palabras creadas por los otros jugadores.\n" +
                "Para jugar Scrabble, necesitas al menos otro jugador. Al jugar el juego, crearás palabras, acumularás puntos, desafiarás a tus oponentes e incluso intercambiarás azulejos (si los tuyos no te sirven). " +
                "Al mismo tiempo, un anotador calculará los puntos de cada jugador para determinar quién ganará al final del juego.</html>");
        panelPrincipal.add(etiqueta);
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton jugarPartidaButton = crearBoton("JUGAR PARTIDA", panelPrincipal);
        JButton reglaScrabbleButton = crearBoton("REGLAS SCRABBLE", panelPrincipal);
        JButton salirButton = crearBoton("SALIR", panelPrincipal);

        jugarPartidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuIniciarPartida(controlador);
            }
        });

        reglaScrabbleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarReglasScrabble();
            }
        });
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(panelPrincipal,
                        "¿Estás seguro de que deseas salir?",
                        "Confirmar salida",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }

            }
        });

        JPanel contenedorPrincipal = new JPanel();
        contenedorPrincipal.setLayout(new BorderLayout());
        contenedorPrincipal.add(panelPrincipal, BorderLayout.CENTER);

        frame.add(contenedorPrincipal);
        frame.setVisible(true);
    }


    private void mostrarReglasScrabble() {
        JOptionPane.showMessageDialog(frame, "1. El juego se juega con dos a cuatro jugadores.\n" +
                "2. Cada jugador recibe siete fichas al azar del conjunto de fichas.\n" +
                "3. El primer jugador coloca una palabra en el tablero, utilizando al menos una de sus fichas.\n" +
                "4. Las palabras deben ser formadas de izquierda a derecha o de arriba hacia abajo.\n" +
                "5. Las palabras deben estar en el diccionario oficial del juego.\n" +
                "6. Las palabras no pueden ser abreviaturas, nombres propios o palabras extranjeras que no estén en el diccionario.\n" +
                "7. Cada letra tiene un valor asignado, y el valor de la palabra es la suma de los valores de las letras.\n" +
                "8. Si un jugador utiliza todas sus fichas en una sola jugada, recibe un bono de 50 puntos.\n" +
                "9. Los jugadores pueden utilizar las palabras ya existentes en el tablero para formar nuevas palabras.\n" +
                "10. Los jugadores pueden utilizar los espacios especiales del tablero para obtener puntos adicionales.\n" +
                "11. Los jugadores pueden intercambiar una o más fichas por otras del conjunto de fichas, pero pierden su turno.\n" +
                "12. El juego termina cuando se han agotado todas las fichas o cuando todos los jugadores han pasado su turno dos veces consecutivas.\n" +
                "13. El jugador con la puntuación más alta al final del juego es el ganador.");
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