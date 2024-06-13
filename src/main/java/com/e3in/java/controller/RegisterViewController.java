package com.e3in.java.controller;

import com.e3in.java.AppConfig;
import com.e3in.java.model.User;
import com.e3in.java.utils.Common;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class RegisterViewController {

    private final UserController userController = new UserController(AppConfig.createUserDAO());

    @FXML
    private TextField textFieldLogin;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordFieldEnsure;

    @FXML
    public void handleConnection() {
        Common.switchScene("ConnectionView", getStage());
    }

    public void handleRegister() {
        String email = textFieldLogin.getText();
        String password = passwordField.getText();
        String passwordEnsure = passwordFieldEnsure.getText();

        if(!password.equals(passwordEnsure)) {
            Common.showAlert(Alert.AlertType.ERROR, "Les mots de passe ne correspondent pas", "Les mots de passe doivent être identiques.");
            return;
        }

        User user = new User(email, password, false);
        if (UserController.checkValidity(user)) {
            boolean userInserted = userController.createUser(user);
            if (userInserted) {
                System.out.println("Connexion réussie ! Bienvenue " + user.getEmail());
                Common.switchScene("ConnectionView", getStage());
            } else {
                System.out.println("Échec d'inscription. Veuillez vérifier les informations.");
            }
        }
        this.resetFields();
    }

    public void resetFields() {
        textFieldLogin.setText("");
        passwordField.setText("");
        passwordFieldEnsure.setText("");
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
