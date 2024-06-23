package com.e3in.java.controller;

import com.e3in.java.AppConfig;
import com.e3in.java.model.User;
import com.e3in.java.utils.Common;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.logging.Logger;


public class RegisterViewController {

    static Logger logger = Logger.getLogger(RegisterViewController.class.getName());

    private final UserController userController = new UserController(AppConfig.getUserDAO());

    @FXML
    private TextField textFieldLogin;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordFieldEnsure;

    @FXML
    private CheckBox userIsAdmin;

    @FXML
    public void handleConnection() {
        Common.switchScene("ConnectionView", getStage());
    }

    public void handleRegister() {
        String email = textFieldLogin.getText();
        String password = passwordField.getText();
        String passwordEnsure = passwordFieldEnsure.getText();
        Boolean isAdmin = userIsAdmin.isSelected();

        if(!password.equals(passwordEnsure)) {
            Common.showAlert(Alert.AlertType.ERROR, "Les mots de passe ne correspondent pas", "Les mots de passe doivent être identiques.");
            return;
        }

        User user = new User(email, password, isAdmin);
        HashMap<String, String> userValidation = UserController.checkValidity(user);
        if (userValidation.isEmpty()) {
            boolean userInserted = userController.createUser(user);
            if (userInserted) {
                logger.info("Connexion réussie ! Bienvenue " + user.getEmail());
                Common.switchScene("ConnectionView", getStage());
            } else {
                logger.warning("Échec d'inscription. Veuillez vérifier les informations.");
            }
        } else {
            Common.showAlert(Alert.AlertType.ERROR, userValidation.get("error"), userValidation.get("message"));
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
