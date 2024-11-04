package org.ahorcado.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PuntuacionesController {

    private List<Puntuacion> puntuaciones = new ArrayList<>();
    private static final String FILE_PATH = "puntuaciones.txt";

    public PuntuacionesController() {
        cargarPuntuaciones();
    }

    public List<Puntuacion> getPuntuaciones() {
        puntuaciones.sort(Comparator.comparingInt(Puntuacion::getPuntos).reversed());
        return puntuaciones;
    }

    public void agregarPuntuacion(String nombre, int puntos) {
        puntuaciones.add(new Puntuacion(nombre, puntos));
    }

    void cargarPuntuaciones() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                puntuaciones.add(new Puntuacion(data[0], Integer.parseInt(data[1])));
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar el archivo de puntuaciones.");
        }
    }

    public void guardarPuntuaciones() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Puntuacion puntuacion : puntuaciones) {
                writer.write(puntuacion.getNombre() + "," + puntuacion.getPuntos());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("No se pudo guardar el archivo de puntuaciones.");
        }
    }

    public class Puntuacion {
        private String nombre;
        private int puntos;

        public Puntuacion(String nombre, int puntos) {
            this.nombre = nombre;
            this.puntos = puntos;
        }

        public String getNombre() {
            return nombre;
        }

        public int getPuntos() {
            return puntos;
        }
    }

}
