package com.e3in.java.controller;

import com.e3in.java.utils.Xml;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

// Apache POI
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.e3in.java.model.Bibliotheque;
import com.e3in.java.model.Livre;

/**
 * Controller de la vue Connexion
 */
public class ConnectionViewController {

   @FXML
    private TextField textFieldLogin;
    @FXML
    private TextField passwordField;
    @FXML
    public void handleConnection() {
        System.out.println("Je me connecte en tant que : " + this.textFieldLogin.getText() + " avec " + this.passwordField.getText() + " en tant que mot de passe");
    }
    @FXML
    public void handleCancel() {
        System.out.print("J'annule mon action");
    }
    @FXML
    public void handleRegister() {
        System.out.print("Je souhaite m'inscrire, donc redirection vers la page d'inscription");
    }
}
