package com.e3in.java.controller;

import com.e3in.java.model.User;
import com.e3in.java.utils.SQLiteConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.e3in.java.utils.Common;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionViewController {

    @FXML
    private TextField textFieldLogin;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void handleConnection() {
        User user = new User();
        String login = textFieldLogin.getText();
        String password = passwordField.getText();
        if (checkLoginValidity(login, password)) {
            user.setEmail(login);
            user.setPassword(password);
            boolean userConnected = loginNewUser(user);
            if (userConnected) {
                Common.switchScene("MainView", getStage());
            }
        }
    }

    public static boolean checkLoginValidity(String email, String password) {
        if (email == null || !email.contains("@")) {
            Common.showAlert(Alert.AlertType.ERROR, "Email invalide", "Insérez une adresse email valide.");
            return false;
        }
        if (password == null || password.isEmpty()) {
            Common.showAlert(Alert.AlertType.ERROR, "Mot de passe invalide", "Le mot de passe ne peut pas être vide.");
            return false;
        }
        return true;
    }

    public static boolean loginNewUser(User user) {
        String sql = "SELECT password FROM user WHERE email = ?";
        try (Connection conn = SQLiteConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getEmail());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(user.getPassword(), hashedPassword)) {
                    Common.showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue " + user.getEmail());
                    return true;
                } else {
                    Common.showAlert(Alert.AlertType.ERROR, "Identifiants invalides", "Email ou mot de passe incorrect. Veuillez réessayer.");
                }
            } else {
                Common.showAlert(Alert.AlertType.ERROR, "Utilisateur introuvable", "Aucun compte trouvé avec l'email fourni.");
            }
        } catch (SQLException e) {
            Common.showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Connexion impossible. Veuillez réessayer."); //TODO:: déconnexion de la bdd en cas d'erreur ?
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    public void handleRegister() {
        Common.switchScene("RegisterView", getStage());
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
