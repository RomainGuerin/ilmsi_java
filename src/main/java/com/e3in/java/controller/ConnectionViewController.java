package com.e3in.java.controller;

import com.e3in.java.AppConfig;
import com.e3in.java.model.User;
import com.e3in.java.utils.Common;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Contrôleur pour la vue de connexion.
 * Gère les interactions utilisateur et les opérations liées à la connexion.
 */
public class ConnectionViewController {

    static Logger logger = Logger.getLogger(ConnectionViewController.class.getName());

    private final UserController userController = new UserController(AppConfig.getUserDAO());

    @FXML
    private TextField textFieldLogin;

    @FXML
    private PasswordField passwordField;

    /**
     * Méthode appelée lors du clic sur le bouton de connexion.
     * Valide les informations de connexion et redirige l'utilisateur si la connexion est réussie.
     */
    public void handleConnection() {
        String email = textFieldLogin.getText();
        String password = passwordField.getText();

        User user = new User(email, password);
        Map<String, String> userValidation = UserController.checkValidity(user);
        if (userValidation.isEmpty()) {
            User userConnected = userController.getUserByEmailPassword(user);
            if (userConnected != null) {
                Common.showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + userConnected.getEmail());
                Common.switchScene("MainView", getStage(), userConnected);
            } else {
                logger.warning("Échec de la connexion. Veuillez vérifier vos identifiants.");
            }
        } else {
            Common.showAlert(Alert.AlertType.ERROR, userValidation.get("error"), userValidation.get("message"));
        }
        this.resetFields();
    }

    /**
     * Méthode appelée lors du clic sur le bouton d'inscription.
     * Redirige l'utilisateur vers la vue d'inscription.
     */
    @FXML
    public void handleRegister() {
        Common.switchScene("RegisterView", getStage());
    }

    /**
     * Réinitialise les champs de texte du formulaire de connexion.
     */
    public void resetFields() {
        textFieldLogin.setText("");
        passwordField.setText("");
    }

    /**
     * Méthode appelée lors du clic sur le bouton pour quitter l'application.
     * Ferme l'application.
     */
    @FXML
    private void handleQuitApp() {
        Common.closeApp(getStage());
    }

    /**
     * Méthode appelée lors du clic sur le bouton pour afficher les informations sur l'application.
     * Affiche une fenêtre d'informations sur l'application.
     */
    @FXML
    private void handleInfos() {
        Common.showAboutPopup();
    }

    /**
     * Méthode pour récupérer la fenêtre principale (Stage).
     *
     * @return La fenêtre principale de l'application.
     */
    private Stage getStage() {
        return (Stage) textFieldLogin.getScene().getWindow();
    }
}
