package ar.edu.unlu.Modelo;

import java.io.*;
import java.util.ArrayList;

public class Diccionario  implements Serializable {
    private static final String NOMBRE_ARCHIVO = "C:\\Users\\Tobias\\IdeaProjects\\Scrabble\\src\\ar\\edu\\unlu\\Modelo/es_dic.txt";
    private ArrayList<String> palabras;

    public Diccionario() {
        palabras = new ArrayList<>();
    }

    public boolean existePalabra(String palabra) {
        if (palabra.contains(" ")) {
            boolean existe = false;

            for (char i = 'a'; i <= 'z'; i++) {
                if (existePalabra(palabra.replaceFirst(" ", String.valueOf(i))))
                    existe = true;
            }
            return existe;
        }
        return palabras.contains(palabra.toLowerCase());
    }

    public void cargarDiccionario() {

        File archivo = new File(NOMBRE_ARCHIVO);
        if (archivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    // Añadir cada palabra al ArrayList, asegurándose que esté en minúsculas
                    palabras.add(linea.trim().toLowerCase());
                }
            } catch (IOException e) {
                System.err.println("Error al cargar el diccionario desde el archivo: " + archivo);
                e.printStackTrace();
            }
        } else {
            System.err.println("El archivo del diccionario no existe: " + archivo);
        }
    }

    // Método para obtener la lista de palabras (si es necesario)
    public ArrayList<String> getPalabras() {
        return palabras;
    }

}