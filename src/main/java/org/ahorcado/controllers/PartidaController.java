package org.ahorcado.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.image.Image;

public class PartidaController {

    private String palabraAdivinar;
    private StringBuilder palabraOculta;
    private int puntuacion;
    private int vidas;
    private final PalabrasController palabrasController;
    private final PuntuacionesController puntuacionesController; // Añadido para el manejo de puntuaciones

    // Atributo para almacenar las imágenes del ahorcado
    private List<Image> imagenesAhorcado = new ArrayList<>();
    private int indiceFallo = 0;
    private List<Character> letrasUsadas; // Añadido para almacenar letras usadas

    public PartidaController(PalabrasController palabrasController, PuntuacionesController puntuacionesController) {
        this.palabrasController = palabrasController;
        this.puntuacionesController = puntuacionesController; // Inicializa el controlador de puntuaciones
        this.letrasUsadas = new ArrayList<>(); // Inicializa la lista de letras usadas
        cargarImagenesAhorcado();
        iniciarNuevaPartida();
    }

    private void cargarImagenesAhorcado() {
        // Carga las imágenes una por una
        for (int i = 1; i <= 9; i++) {
            imagenesAhorcado.add(new Image(getClass().getResourceAsStream("/images/" + i + ".png")));
        }
    }

    // Metodo para obtener la imagen actual del ahorcado
    public Image getImagenAhorcado() {
        return imagenesAhorcado.get(indiceFallo);
    }

    public void iniciarNuevaPartida() {
        palabraAdivinar = seleccionarPalabra();
        palabraOculta = new StringBuilder("_".repeat(palabraAdivinar.length()));
        puntuacion = 0;
        vidas = 5;  // Puedes ajustar la cantidad de vidas según prefieras
        letrasUsadas.clear(); // Limpiar letras usadas al iniciar nueva partida
    }

    private String seleccionarPalabra() {
        List<String> palabras = palabrasController.getPalabras();
        if (palabras.isEmpty()) {
            throw new IllegalStateException("La lista de palabras está vacía. No se puede iniciar el juego.");
        }
        Random random = new Random();
        return palabras.get(random.nextInt(palabras.size()));
    }


    public boolean adivinarLetra(char letra) {
        if (letrasUsadas.contains(letra)) {
            return false; // La letra ya fue usada
        }
        letrasUsadas.add(letra); // Añadir la letra a las usadas
        boolean acierto = false;
        for (int i = 0; i < palabraAdivinar.length(); i++) {
            if (palabraAdivinar.charAt(i) == letra) {
                palabraOculta.setCharAt(i, letra);
                acierto = true;
                puntuacion++;
            }
        }
        if (!acierto) {
            vidas--;
            if (indiceFallo < imagenesAhorcado.size() - 1) {
                indiceFallo++;
            }
        }
        return acierto;
    }

    public boolean resolverPalabra(String intento) {
        if (intento.equalsIgnoreCase(palabraAdivinar)) {
            puntuacion += palabraAdivinar.length() - palabraOculta.toString().replace("_", "").length();
            palabraOculta = new StringBuilder(palabraAdivinar);
            return true;
        } else {
            vidas--;
            return false;
        }
    }

    public boolean partidaTerminada() {
        if (vidas <= 0 || palabraOculta.toString().equals(palabraAdivinar)) {
            if (palabraOculta.toString().equals(palabraAdivinar)) {
                puntuacionesController.agregarPuntuacion("Jugador", puntuacion); // Guarda la puntuación al terminar
                puntuacionesController.guardarPuntuaciones(); // Persistir puntuaciones
            }
            return true;
        }
        return false;
    }

    public String getPalabraOculta() {
        return palabraOculta.toString();
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public int getVidas() {
        return vidas;
    }

    public String getLetrasUsadas() {
        return letrasUsadas.stream()
                .map(String::valueOf)
                .reduce("", (a, b) -> a + " " + b).trim(); // Devuelve las letras usadas como un string
    }
}
