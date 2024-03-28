package com.e3in.java.utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.e3in.java.controller.MainViewController;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Common {
    /**
     * Méthode pour afficher une alerte d'erreur
     * 
     * @param title   Le titre de l'alerte.
     * @param content Le contenu de l'alerte.
     */
    public static void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image icon = new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/alert.png")));
        stage.getIcons().add(icon);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Méthode pour choisir l'emplacement de sauvegarde.
     * 
     * @param extension L'extension du fichier de sauvegarde.
     * @param stage     La fenêtre de l'application.
     * @return Le chemin absolu de l'emplacement de sauvegarde sélectionné.
     */
    public static String getPathFile(String extension, Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un emplacement de fichier");
        String description = extension.toUpperCase() + " files (*." + extension.toLowerCase() + ")";
        String extensionFilter = "*." + extension.toLowerCase();
        fileChooser.getExtensionFilters().add(new ExtensionFilter(description, extensionFilter));
        File file = fileChooser.showSaveDialog(stage);
        return file != null ? file.getAbsolutePath() : null;
    }

    /**
     * Méthode pour obtenir la date et l'heure actuelle.
     * 
     * @return La date et l'heure actuelle au format "dd/MM/yyyy HH:mm".
     */
    public static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return formatter.format(LocalDateTime.now());
    }
}
