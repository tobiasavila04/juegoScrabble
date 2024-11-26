package ar.edu.unlu;

import ar.edu.unlu.Modelo.IScrabbleGame;
import ar.edu.unlu.Modelo.Scrabblegame;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.servidor.Servidor;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class APPServidor {
    /**
     * Se inicia un servidor limpio. Es decir, es una partida en 0, sin nada cargado. Un modelo limpio.
     */
    public static void main(String[] args) throws RemoteException {
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );
        String cantidadJugadoresStr = (String) JOptionPane.showInputDialog(
                null,
                "Ingrese la cantidad de jugadores (debe ser entre 2 y 4)", "Cantidad de Jugadores",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                "2"
        );
        int cantidadJugadores = 2; // Valor por defecto
        try {
            cantidadJugadores = Integer.parseInt(cantidadJugadoresStr);
            if (cantidadJugadores < 2 || cantidadJugadores > 4) {
                JOptionPane.showMessageDialog(null, "La cantidad de jugadores debe estar entre 2 y 4.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        Scrabblegame modelo = Scrabblegame.getInstancia();
        Servidor servidor = new Servidor(ip, Integer.parseInt(port));
        try {
            modelo.setCantidadDeJugadores(cantidadJugadores);
            servidor.iniciar(modelo);
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Se ha iniciado correctamente el servidor.\nPara unirte a la partida, volvé al menú principal y seleccioná la opción 'Unirse a un servidor',\ncompletando con los datos de tu computadora/red.'", "Servidor iniciado.", JOptionPane.INFORMATION_MESSAGE);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No se ha podido iniciar correctamente el servidor. Vuelva a intentar.", "Error al iniciar el servidor", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (RMIMVCException e) {
            throw new RuntimeException(e);
        }
    }
}

    /**
     * Se inicia un servidor en base a un modelo ya creado desde antes.
     * Esto lo hacemos para reanudar una partida.
     *
     * @param modelo Es el Modelo que se va utilizar para reanudar la partida.
     */
  /*  public APPServidor(Scrabblegame modelo) {
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchará peticiones el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchará peticiones el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );
        Servidor servidor = new Servidor(ip, Integer.parseInt(port));
        try {
            servidor.iniciar(modelo);
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Se ha iniciado correctamente el servidor.\nPara unirte a la partida, volvé al menú principal y seleccioná la opción 'Unirse a un servidor',\ncompletando con los datos de tu computadora/red.'", "Servidor iniciado.", JOptionPane.INFORMATION_MESSAGE);
        } catch (RMIMVCException | RemoteException e) {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "No se ha podido iniciar correctamente el servidor. Vuelva a intentar.", "Error al iniciar el servidor", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}*/
