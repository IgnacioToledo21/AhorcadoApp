package org.ahorcado.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PalabrasController {

    private List<String> palabras = new ArrayList<>();
    private static final String FILE_PATH = "palabras.txt";

    public PalabrasController() {
        cargarPalabras();
    }

    public List<String> getPalabras() {
        return palabras;
    }

    public void agregarPalabra(String palabra) {
        if (!palabra.isEmpty() && !palabras.contains(palabra)) {
            palabras.add(palabra);
        }
    }

    public void eliminarPalabra(String palabra) {
        palabras.remove(palabra);
    }

    void cargarPalabras() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                palabras.add(line);
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar el archivo de palabras.");
        }
    }

    public void guardarPalabras() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String palabra : palabras) {
                writer.write(palabra);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("No se pudo guardar el archivo de palabras.");
        }
    }
}
