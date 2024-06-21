package com.e3in.java.utils;

import com.e3in.java.controller.MainViewController;
import com.e3in.java.model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Common {
    /**
     * Ferme l'application.
     * @param stage La fenêtre (Stage) de l'application.
     */
    public static void closeApp(Stage stage) {
        stage.close();
    }

    /**
     * Affiche une fenêtre contextuelle "À propos" avec des informations sur l'application.
     */
    public static void showAboutPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Common.class.getResource("/view/AboutView.fxml"));
            VBox content = fxmlLoader.load();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText(null);
            alert.getDialogPane().setContent(content);
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Erreur de chargement de la page About: " + e.getMessage());
        }
    }

    /**
     * Change la scène actuelle à une nouvelle scène spécifiée par la page.
     * @param page La page vers laquelle rediriger.
     * @param stage La fenêtre (Stage) de l'application.
     */
    public static void switchScene(String page, Stage stage) {
        switchScene(page, stage, null);
    }

    /**
     * Change la scène actuelle à une nouvelle scène spécifiée par la page avec un utilisateur.
     * @param page La page vers laquelle rediriger.
     * @param stage La fenêtre (Stage) de l'application.
     * @param user L'utilisateur actuel.
     */
    public static void switchScene(String page, Stage stage, User user) {
        System.out.println("Redirection vers la page " + page);
        try {
            FXMLLoader loader = new FXMLLoader(Common.class.getResource("/view/" + page + ".fxml"));
            Parent registerView = loader.load();

            Object controller = loader.getController();
            if (controller instanceof UserAwareController userAwareController) {
                userAwareController.setUser(user);
            }

            Scene registerScene = new Scene(registerView, 1200, 600);
            stage.setScene(registerScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche une alerte.
     * @param alterType Le type d'alerte.
     * @param title Le titre de l'alerte.
     * @param content Le contenu de l'alerte.
     */
    public static void showAlert(Alert.AlertType alterType, String title, String content) {
        Alert alert = new Alert(alterType);
        alert.setTitle(title);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image icon = new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/alert.png")));
        stage.getIcons().add(icon);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Retourne la date et l'heure actuelles sous forme de chaîne formatée.
     * @return La date et l'heure actuelles au format "dd/MM/yyyy HH:mm".
     */
    public static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return formatter.format(LocalDateTime.now());
    }
}
