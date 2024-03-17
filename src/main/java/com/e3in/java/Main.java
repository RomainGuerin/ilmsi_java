package com.e3in.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Classe principale de l'application
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainView.fxml")));
        Scene scene = new Scene(root, 1100, 500);

        primaryStage.setTitle("Java FX Bibliothèque");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Point d'entrée de l'application.
     * 
     * @param args Les arguments pour lancer l'application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
