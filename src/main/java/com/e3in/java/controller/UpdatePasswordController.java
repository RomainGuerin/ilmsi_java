package com.e3in.java.controller;

import com.e3in.java.AppConfig;
import com.e3in.java.model.User;
import com.e3in.java.utils.Common;
import com.e3in.java.utils.UserAwareController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Contrôleur pour la vue de mise à jour du mot de passe.
 * Gère les interactions utilisateur et les opérations liées à la mise à jour du mot de passe.
 */
public class UpdatePasswordController implements UserAwareController {

    private final UserController userController = new UserController(AppConfig.getUserDAO());

    @FXML
    private TextField textFieldLogin;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField newPasswordFieldEnsure;

    private User connectedUser;

    /**
     * Méthode de l'interface UserAwareController.
     * Permet de définir l'utilisateur connecté à partir d'une autre vue.
     *
     * @param user L'utilisateur connecté.
     */
    @Override
    public void setUser(User user) {
        connectedUser = user;
    }

    /**
     * Méthode appelée lors du clic sur le bouton pour retourner à la page d'accueil.
     * Redirige l'utilisateur vers la vue principale (MainView).
     */
    @FXML
    public void handleSwitchHome() {
        Common.switchScene("MainView", getStage(), connectedUser);
    }

    /**
     * Méthode appelée lors du clic sur le bouton pour mettre à jour le mot de passe.
     * Valide les informations et met à jour le mot de passe de l'utilisateur connecté.
     */
    public void handleUpdatePassword() {
        String email = textFieldLogin.getText();
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String newPasswordEnsure = newPasswordFieldEnsure.getText();

        // Vérifie que tous les champs sont remplis
        if (email.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || newPasswordEnsure.isEmpty()) {
            Common.showAlert(Alert.AlertType.ERROR, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        } else if (!email.equals(connectedUser.getEmail())) {
            Common.showAlert(Alert.AlertType.ERROR, "Email incorrect", "Veuillez saisir l'email de votre compte.");
            return;
        } else if (oldPassword.equals(newPassword)) {
            Common.showAlert(Alert.AlertType.ERROR, "L'ancien mot de passe est identique au nouveau", "Le nouveau mot de passe doit être différent de l'ancien.");
            return;
        } else if (!newPassword.equals(newPasswordEnsure)) {
            Common.showAlert(Alert.AlertType.ERROR, "Les mots de passe ne correspondent pas", "Les mots de passe doivent être identiques.");
            return;
        }

        // Crée les objets User pour l'ancien et le nouveau mot de passe
        User oldUser = new User(email, oldPassword, false);
        User newUser = new User(email, newPassword, false);

        // Vérifie la validité du nouveau mot de passe
        if (UserController.checkValidity(newUser)) {
            // Récupère l'utilisateur connecté à partir des identifiants fournis
            User userConnected = userController.getUserByEmailPassword(oldUser);
            if (userConnected != null) {
                // Met à jour le mot de passe dans la base de données
                if(userController.updatePassword(newUser)){
                    Common.showAlert(Alert.AlertType.INFORMATION, "Mise à jour du mot de passe", "Le mot de passe a bien été mis à jour.");
                } else {
                    Common.showAlert(Alert.AlertType.ERROR, "Erreur - Mise à jour du mot de passe", "Impossible de mettre à jour le mot de passe. Vérifiez les informations.");
                }
            } else {
                System.out.println("Les identifiants ne correspondent pas. Veuillez réessayer.");
            }
        }
        // Réinitialise les champs de texte après la mise à jour du mot de passe
        this.resetFields();
    }

    /**
     * Réinitialise les champs de texte du formulaire de mise à jour du mot de passe.
     */
    public void resetFields() {
        textFieldLogin.setText("");
        oldPasswordField.setText("");
        newPasswordField.setText("");
        newPasswordFieldEnsure.setText("");
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
