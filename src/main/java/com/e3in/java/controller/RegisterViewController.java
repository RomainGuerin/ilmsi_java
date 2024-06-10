package com.e3in.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller de la vue inscription
 */
public class RegisterViewController {

   @FXML
    private TextField textFieldLogin;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordFieldEnsure;
    @FXML
    public void handleRegister() {
        System.out.println("Je m'inscrit en tant que : " + this.textFieldLogin.getText() + " avec " + this.passwordField.getText() + " en tant que mot de passe et " + this.passwordFieldEnsure.getText());
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
