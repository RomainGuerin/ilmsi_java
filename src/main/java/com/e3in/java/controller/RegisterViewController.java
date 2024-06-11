package com.e3in.java.controller;

import com.e3in.java.AppConfig;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.etsi.uri.x01903.v13.SignerRoleType;

import java.util.HashMap;

/**
 * Controller de la vue inscription
 */
public class RegisterViewController {
    private final UserController userController = new UserController(AppConfig.createUserDAO());

   @FXML
    private TextField textFieldLogin;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordFieldEnsure;
    @FXML
    public void handleRegister() {
        String email = textFieldLogin.getText();
        String password = passwordField.getText();
        String passwordEnsure = passwordFieldEnsure.getText();
        if(!password.equals(passwordEnsure)) {
            System.out.println("Échec de la connexion. Veuillez vérifier vos identifiants.");
            return;
        }
        HashMap<String, String> dataUser = new HashMap<>();
        dataUser.put("email", email);
        dataUser.put("password", password);
        dataUser.put("type", "user");
        HashMap<String, String> user = userController.createUser(dataUser);
        if (!user.isEmpty()) {
            System.out.println("Connexion réussie ! Bienvenue " + user.get("email"));
        } else {
            System.out.println("Échec de la connexion. Veuillez vérifier vos identifiants.");
        }
    }
    @FXML
    public void handleConnection() {
        System.out.print("Je souhaite me connecter, donc redirection vers la page de connexion");
    }
    @FXML
    public void handleCancel() {
        System.out.print("J'annule mon action");
    }
}
