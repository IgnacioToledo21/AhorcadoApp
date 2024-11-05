package org.ahorcado.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    @FXML
    private AnchorPane Root;
    @FXML
    private TabPane Tabpane;
    @FXML
    private ListView<String> wordsList;
    @FXML
    private ListView<String> scoresList;
    @FXML
    private Button LetterButton, ResolveButton, addButton, removeButton;
    @FXML
    private Label guessedLetters, usedLetters, pointsLabel;
    @FXML
    private TextField letterInput;
    @FXML
    private ImageView hangmanImage;

    private PartidaController partidaController;
    private PuntuacionesController puntuacionesController;
    private PalabrasController palabrasController;

    // Constructor
    public RootController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        palabrasController = new PalabrasController();
        puntuacionesController = new PuntuacionesController();
        partidaController = new PartidaController(palabrasController, puntuacionesController);
        palabrasController.cargarPalabras();
        puntuacionesController.cargarPuntuaciones();
        actualizarListaPalabras();
        actualizarPuntuaciones();
    }

    @FXML
    void onAddAction(ActionEvent event) {
        // Crear un TextInputDialog para ingresar una palabra
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Añadir Palabra");
        dialog.setHeaderText("¿Qué palabra deseas añadir?");
        dialog.setContentText("Palabra:");

        // Mostrar el diálogo y obtener la respuesta
        dialog.showAndWait().ifPresent(palabra -> {
            if (palabra != null && !palabra.isEmpty()) {
                // Añadir la palabra al controlador de palabras
                palabrasController.agregarPalabra(palabra);
                palabrasController.guardarPalabras(); // Guardar las palabras en el archivo
                actualizarListaPalabras(); // Actualizar el ListView con las palabras añadidas
            }
        });
    }


    @FXML
    void onLetterAction(ActionEvent event) {
        String letter = letterInput.getText();
        if (letter != null && !letter.isEmpty() && letter.length() == 1) {
            partidaController.adivinarLetra(letter.charAt(0));
            actualizarJuego();
            letterInput.clear();
        }
    }

    @FXML
    void onRemoveAction(ActionEvent event) {
        String selectedWord = wordsList.getSelectionModel().getSelectedItem();
        if (selectedWord != null) {
            palabrasController.eliminarPalabra(selectedWord);
            palabrasController.guardarPalabras();
            actualizarListaPalabras();
        }
    }

    @FXML
    void onResolveAction(ActionEvent event) {
        String guessedWord = letterInput.getText();
        if (guessedWord != null && !guessedWord.isEmpty()) {
            boolean acierto = partidaController.resolverPalabra(guessedWord);
            if (acierto) {
                puntuacionesController.agregarPuntuacion("Jugador", partidaController.getPuntuacion());
                puntuacionesController.guardarPuntuaciones();
                actualizarPuntuaciones();
            }
            actualizarJuego();
            letterInput.clear();
        }
    }

    private void actualizarListaPalabras() {
        wordsList.getItems().setAll(palabrasController.getPalabras());
    }

    private void actualizarPuntuaciones() {
        scoresList.getItems().setAll(puntuacionesController.getPuntuaciones().stream()
                .map(p -> p.getNombre() + ": " + p.getPuntos())
                .toList());
    }

    @FXML
    private void actualizarJuego() {
        guessedLetters.setText(partidaController.getPalabraOculta());
        pointsLabel.setText(String.valueOf(partidaController.getPuntuacion()));
        hangmanImage.setImage(partidaController.getImagenAhorcado());
        usedLetters.setText(partidaController.getLetrasUsadas()); // Muestra las letras usadas

        // Verifica si la partida ha terminado
        if (partidaController.partidaTerminada(this::mostrarReinicioDialog)) {
            // Si la partida terminó, ya se mostró el diálogo
        }
    }

    private void mostrarReinicioDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("¡Juego Terminado!");
        alert.setHeaderText("¿Quieres volver a jugar?");
        alert.setContentText("Haz clic en 'Aceptar' para reiniciar.");

        ButtonType buttonTypeOne = new ButtonType("Aceptar");
        ButtonType buttonTypeTwo = new ButtonType("Cancelar");

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeOne) {
                partidaController.iniciarNuevaPartida(); // Reinicia la partida
                actualizarJuego(); // Actualiza la interfaz
            }
        });
    }


    public AnchorPane getRoot() {
        return Root;
    }
}
