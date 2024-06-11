package com.e3in.java.controller;

import com.e3in.java.AppConfig;
import com.e3in.java.dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

/**
 * Controller de la vue Connexion
 */
public class ConnectionViewController {
    private final UserController userController = new UserController(AppConfig.createUserDAO());


    @FXML
    private TextField textFieldLogin;
    @FXML
    private PasswordField passwordField;

    @FXML
    public void handleConnection() {
        String email = textFieldLogin.getText();
        String password = passwordField.getText();

        HashMap<String, String> user = userController.getUserByEmailPassword(email, password);
        if (!user.isEmpty()) {
            System.out.println("Connexion réussie ! Bienvenue " + user.get("email") + user);
        } else {
            System.out.println("Échec de la connexion. Veuillez vérifier vos identifiants.");
        }
    }

    @FXML
    public void handleCancel() {
        System.out.println("Action annulée");
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        System.out.println("Redirection vers la page d'inscription");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RegisterView.fxml"));
        try {
            Parent registerView = loader.load();
            Scene registerScene = new Scene(registerView);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(registerScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}