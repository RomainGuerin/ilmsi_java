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

import java.util.Map;
import java.util.logging.Logger;

public class UpdatePasswordController implements UserAwareController {

    static Logger logger = Logger.getLogger(UpdatePasswordController.class.getName());

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

    @Override
    public void setUser(User user) {
        connectedUser = user;
    }

    @FXML
    public void handleSwitchHome() {
        Common.switchScene("MainView", getStage(), connectedUser);
    }

    public void handleUpdatePassword() {
        String email = textFieldLogin.getText();
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String newPasswordEnsure = newPasswordFieldEnsure.getText();

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

        User oldUser = new User(email, oldPassword, false);
        User newUser = new User(email, newPassword, false);
        Map<String, String> userValidation = UserController.checkValidity(newUser);
        if (userValidation.isEmpty()) {
            AppConfig.getDAOManager().setOnline(true); // Nécessaire pour utiliser UserDAO
            User userConnected = userController.getUserByEmailPassword(oldUser);
            if (userConnected != null) {
                if(userController.updatePassword(newUser)){
                    Common.showAlert(Alert.AlertType.INFORMATION, "Mise à jour du mot de passe", "Le mot de passe à bien été mise à jour.");
                } else {
                    Common.showAlert(Alert.AlertType.ERROR, "Erreur - Mise à jour du mot de passe", "Impossible de mettre à jour le mot de passe. Vérifier les informations.");
                }
            } else {
                logger.warning("Les identifiants ne correspondent pas. Veuillez réessayer.");
            }
        } else {
            Common.showAlert(Alert.AlertType.ERROR, userValidation.get("error"), userValidation.get("message"));
        }
        this.resetFields();
    }

    public void resetFields() {
        AppConfig.getDAOManager().setOnline(false);
        textFieldLogin.setText("");
        oldPasswordField.setText("");
        newPasswordField.setText("");
        newPasswordFieldEnsure.setText("");
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
