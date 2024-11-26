package ar.edu.unlu.Vista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mensaje extends JDialog {
    private JPanel contentPane; //panel donde se agregan los componentes visuales
    private JButton buttonOK;
    private JLabel lblMensaje; //contiene el mensaje

    public Mensaje(String mensaje) {
        setContentPane(contentPane);
        setModal(true); //indica que el jugador no puede interactuar con otras ventanas hasta que no cierre esta
        getRootPane().setDefaultButton(buttonOK);
        lblMensaje.setText(mensaje); //se muestra el mensaje en la ventana emergente
        setLocationRelativeTo(null);
        pack();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }
}
