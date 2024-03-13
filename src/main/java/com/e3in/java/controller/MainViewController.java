package com.e3in.java.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainViewController {
    @FXML
    private TableView<String> tableView;

    private String xmlFilePath = "";
    
    public MainViewController() { }

    @FXML
    private void handleLoadFile() {
        String xmlFilePath = "";
        xmlFilePath = chooseFile();
        if (xmlFilePath == null || xmlFilePath.isEmpty()) {
            showErrorAlert("Erreur", "Aucun fichier sélectionné");
            return;
        }

        XmlValidator.validateXml(xmlFilePath);
        this.xmlFilePath = xmlFilePath;
    }

    @FXML
    private void handleInfos() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AboutView.fxml"));
            VBox content = fxmlLoader.load();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText(null);
            alert.getDialogPane().setContent(content);
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        String extension = "*.xml";
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("XML files ("+extension+")", extension);
        fileChooser.getExtensionFilters().add(filter);

        File selectedFile = fileChooser.showOpenDialog(getStage());

        return selectedFile == null ? null : selectedFile.getAbsolutePath();
    }

    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

    protected static void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
