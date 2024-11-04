package org.ahorcado;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ahorcado.controllers.RootController;

public class AhorcadoApp extends Application {

    private RootController rootController = new RootController();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Ahorcado");
        primaryStage.setScene(new Scene(rootController.getRoot())); // rootController.getRoot() debe devolver un nodo raíz (como un AnchorPane)
        primaryStage.show();
    }
}
