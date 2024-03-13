package com.e3in.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
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
            // Error
            return;
        }

        XmlUtils.validateXml(xmlFilePath);
        this.xmlFilePath = xmlFilePath;
        LibraryController lib = new LibraryController(xmlFilePath);
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
}
