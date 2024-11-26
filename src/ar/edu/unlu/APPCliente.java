package ar.edu.unlu;

import ar.edu.unlu.Controlador.ScrabbleControlador;
import ar.edu.unlu.Vista.IVista;
import ar.edu.unlu.Vista.VistaConsola;

import ar.edu.unlu.Vista.VistaGrafica;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import javax.swing.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class APPCliente {
  //  static int contadorJugadores = 1;
    public static void main(String[] args) throws RemoteException {
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("Interfáz gráfica");
        opciones.add("Consola");

        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el cliente", "IP del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el cliente", "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );
        String ipServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la corre el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );
        String portServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que corre el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );
        String interfaz = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione como quiere visualizar el juego", "Interfaz gráfica",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones.toArray(),
                null
        );
        String nombreJugador = (String) JOptionPane.showInputDialog(
                null,
                "Ingrese nombre del jugador: ",
                "Nombre del jugador",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                "jugador 1"
        );

       /* if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
            nombreJugador = "Jugador" + contadorJugadores; // Nombre único si no ingresa nada
        }
        contadorJugadores++;*/


        ScrabbleControlador controlador = new ScrabbleControlador();
        IVista vista;
        if (interfaz.equals("Consola")) {
            vista = new VistaConsola(controlador, nombreJugador);
        } else {
            vista = new VistaGrafica(controlador, nombreJugador);
        }

        Cliente cliente = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
        try {
            cliente.iniciar(controlador);
            vista.iniciarVista();
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Uno de los puertos seleccionados ya se encuentra en uso. Vuelva a intentar unirse con otro.", "Error al unirse el servidor", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (RMIMVCException | IOException e) {
            throw new RuntimeException(e);
        }
    }


}
