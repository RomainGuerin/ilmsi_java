package com.e3in.java.controller;

import com.e3in.java.model.User;
import com.e3in.java.utils.Common;
import com.e3in.java.utils.SQLiteConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class RegisterViewController {

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

    @FXML
    public void handleRegister() {
        User user = new User();
        String login = textFieldLogin.getText();
        String password = passwordField.getText();
        String passwordEnsure = passwordFieldEnsure.getText();
        if (checkRegisterValidity(login, password, passwordEnsure)) {
            user.setEmail(login);
            user.setPassword(password);
            boolean userInserted = insertNewUser(user);
            if (userInserted) {
                Common.switchScene("ConnectionView", getStage());
            }
        }
    }

    public static boolean checkRegisterValidity(String email, String password, String passwordEnsure) {
        if (email == null || !email.contains("@")) {
            Common.showAlert(Alert.AlertType.ERROR, "Email invalide", "Insérer une adresse email valide.");
            return false;
        }
        if (password == null || password.isEmpty()) {
            Common.showAlert(Alert.AlertType.ERROR, "Mot de passe invalide", "Le mot de passe ne peut pas être vide.");
            return false;
        }
        if (!password.equals(passwordEnsure)) {
            Common.showAlert(Alert.AlertType.ERROR, "Les mots de passe ne correspondent pas", "Les mots de passe doivent être identiques.");
            return false;
        }
        return true;
    }

    public static boolean insertNewUser(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        String sql = "INSERT INTO user (email, password) VALUES (?, ?)";
        try (Connection conn = SQLiteConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, hashedPassword);
            pstmt.executeUpdate();
            Common.showAlert(Alert.AlertType.INFORMATION, "Inscription réussie", "Utilisateur enregistré.");
            return true;
        } catch (SQLException e) {
            Common.showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Connexion impossible. Veuillez réessayer."); //TODO: déconnexion de la bdd en cas d'erreur ?
            e.printStackTrace();
        }
        return false;
    }


    // Ferme l'application.
    @FXML
    private void handleQuitApp() {
        Common.closeApp(getStage());
    }

    // Affiche une fenêtre d'informations sur l'application.
    @FXML
    private void handleInfos() {
        Common.showAboutPage();
    }

    // Méthode pour récupérer la fenêtre principale
    private Stage getStage() {
        return (Stage) textFieldLogin.getScene().getWindow();
    }
}
