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

/**
 * Contrôleur pour la vue d'inscription.
 * Gère les interactions utilisateur et les opérations liées à l'inscription.
 */
public class RegisterViewController {

    private final UserController userController = new UserController(AppConfig.getUserDAO());

    @FXML
    private TextField textFieldLogin;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordFieldEnsure;

    @FXML
    private CheckBox userIsAdmin;

    /**
     * Méthode appelée lors du clic sur le lien de connexion.
     * Redirige l'utilisateur vers la vue de connexion.
     */
    @FXML
    public void handleConnection() {
        Common.switchScene("ConnectionView", getStage());
    }

    /**
     * Méthode appelée lors du clic sur le bouton d'inscription.
     * Valide les informations d'inscription et crée un nouvel utilisateur si valide.
     */
    public void handleRegister() {
        String email = textFieldLogin.getText();
        String password = passwordField.getText();
        String passwordEnsure = passwordFieldEnsure.getText();
        boolean isAdmin = userIsAdmin.isSelected();

        // Vérifie que les mots de passe correspondent
        if (!password.equals(passwordEnsure)) {
            Common.showAlert(Alert.AlertType.ERROR, "Les mots de passe ne correspondent pas", "Les mots de passe doivent être identiques.");
            return;
        }

        User user = new User(email, password, isAdmin);
        
        // Vérifie la validité des informations d'utilisateur
        if (UserController.checkValidity(user)) {
            boolean userInserted = userController.createUser(user);
            if (userInserted) {
                // Affiche un message de réussite et redirige vers la vue de connexion
                System.out.println("Inscription réussie ! Bienvenue " + user.getEmail());
                Common.switchScene("ConnectionView", getStage());
            } else {
                System.out.println("Échec d'inscription. Veuillez vérifier les informations.");
            }
        }
        // Réinitialise les champs de texte après tentative d'inscription
        this.resetFields();
    }

    /**
     * Réinitialise les champs de texte du formulaire d'inscription.
     */
    public void resetFields() {
        textFieldLogin.setText("");
        passwordField.setText("");
        passwordFieldEnsure.setText("");
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
