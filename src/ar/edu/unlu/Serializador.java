package ar.edu.unlu;

import ar.edu.unlu.Modelo.Jugador;

import java.io.*;
import java.util.ArrayList;

public class Serializador {
    private static final String ARCHIVO_GUARDAR_JUGADORES = "guardarJugadores.dat";
    private static final String ARCHIVO_GUARDAR_PARTIDA = "guardarpartida.dat";

    public static void guardarPartida(ArrayList<DatosPartidaGuardada> partidas) {
        guardarObjeto(ARCHIVO_GUARDAR_PARTIDA, partidas);
    }

    public static void guardarJugadores(ArrayList<Jugador> jugadores) {
        guardarObjeto(ARCHIVO_GUARDAR_JUGADORES, jugadores);
    }

    public static ArrayList<DatosPartidaGuardada> cargarPartidaGuardada() {
        return cargarObjeto(ARCHIVO_GUARDAR_PARTIDA);
    }

    public static ArrayList<Jugador> cargarJugadorHistorico() {
        return cargarObjeto(ARCHIVO_GUARDAR_JUGADORES);
    }

    private static void guardarObjeto(String archivo, Object objeto) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(archivo))) {
            salida.writeObject(objeto);
        } catch (IOException e) {
            System.err.println("Error al guardar en el archivo: " + archivo);
            e.printStackTrace();
        }
    }

    private static <T> T cargarObjeto(String archivo) {
        File file = new File(archivo);
        if (file.exists()) {
            try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))) {
                return (T) entrada.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar desde el archivo: " + archivo);
                e.printStackTrace();
            }
        }
        return null;
    }
}

