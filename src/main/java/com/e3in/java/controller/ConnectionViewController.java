package com.e3in.java.controller;

import com.e3in.java.AppConfig;
import com.e3in.java.model.User;
import com.e3in.java.utils.Common;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConnectionViewController {

    private final UserController userController = new UserController(AppConfig.getUserDAO());

    @FXML
    private TextField textFieldLogin;

    @FXML
    private PasswordField passwordField;

    public void handleConnection() {
        String email = textFieldLogin.getText();
        String password = passwordField.getText();

        User user = new User(email, password);
        if (UserController.checkValidity(user)) {
            User userConnected = userController.getUserByEmailPassword(user);
            if (userConnected != null) {
                System.out.println("Connexion réussie ! Bienvenue " + user.getEmail() + "!");
                Common.switchScene("MainView", getStage(), userConnected);
            } else {
                System.out.println("Échec de la connexion. Veuillez vérifier vos identifiants.");
            }
        }
        this.resetFields();
    }

    @FXML
    public void handleRegister() {
        Common.switchScene("RegisterView", getStage());
    }

    public void resetFields() {
        textFieldLogin.setText("");
        passwordField.setText("");
    }

    // Ferme l'application.
    @FXML
    private void handleQuitApp() {
        Common.closeApp(getStage());
    }

    // Affiche une fenêtre d'informations sur l'application.
    @FXML
    private void handleInfos() {
        Common.showAboutPopup();
    }

    // Méthode pour récupérer la fenêtre principale
    private Stage getStage() {
        return (Stage) textFieldLogin.getScene().getWindow();
    }
}
