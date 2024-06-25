package com.e3in.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Classe principale de l'application JavaFX.
 * Cette classe initialise et lance l'interface utilisateur de l'application.
 */
public class Main extends Application {

    /**
     * Point d'entrée de l'application JavaFX.
     * Cette méthode est appelée lors du lancement de l'application.
     *
     * @param primaryStage La scène principale de l'application.
     * @throws Exception Si une erreur survient lors du chargement de la vue FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Chargement de la vue FXML
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ConnectionView.fxml")));
        
        // Création de la scène avec des dimensions de 1200x600
        Scene scene = new Scene(root, 1200, 600);
        
        // Chargement de l'icône de l'application
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png")));
        
        // Configuration de la scène principale
        primaryStage.setTitle("Java FX Bibliothèque");
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        
        // Affichage de la scène principale
        primaryStage.show();
    }

    /**
     * Point d'entrée principal de l'application.
     * Cette méthode lance l'application JavaFX.
     *
     * @param args Les arguments pour lancer l'application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
