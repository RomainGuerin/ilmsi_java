package com.e3in.java.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

import com.e3in.java.model.Bibliotheque;
import com.e3in.java.model.Livre;

public class MainViewController {

    private String xmlFilePath = "";
    
    @FXML
    private TableView<Livre> tableView;
    private TableColumn<Livre, String> titreColumn;
    private TableColumn<Livre, String> auteurColumn;
    private TableColumn<Livre, String> presentationColumn;
    private TableColumn<Livre, Integer> parutionColumn;
    private TableColumn<Livre, Integer> colonneColumn;
    private TableColumn<Livre, Integer> rangeeColumn;
    
    public MainViewController() { }

    @FXML
    public void initialize() {
        // Initialiser les colonnes dans la méthode initialize
        titreColumn = new TableColumn<>("Titre");
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));

        auteurColumn = new TableColumn<>("Auteur");
        auteurColumn.setCellValueFactory(new PropertyValueFactory<>("auteur"));

        presentationColumn = new TableColumn<>("Présentation");
        presentationColumn.setCellValueFactory(new PropertyValueFactory<>("presentation"));

        parutionColumn = new TableColumn<>("Parution");
        parutionColumn.setCellValueFactory(new PropertyValueFactory<>("parution"));

        colonneColumn = new TableColumn<>("Colonne");
        colonneColumn.setCellValueFactory(new PropertyValueFactory<>("colonne"));

        rangeeColumn = new TableColumn<>("Rangée");
        rangeeColumn.setCellValueFactory(new PropertyValueFactory<>("rangee"));

        // Ajouter les colonnes à la TableView
        tableView.getColumns().addAll(List.of(titreColumn, auteurColumn, presentationColumn, parutionColumn, colonneColumn, rangeeColumn));
    }

    @FXML
    private void handleLoadFile() {
        String xmlFilePath = "";
        xmlFilePath = chooseFile();
        if (xmlFilePath == null || xmlFilePath.isEmpty()) {
            // Error
            return;
        }

        boolean isValid = XmlUtils.validateXml(xmlFilePath);
        if (!isValid) {
            // Handle invalid XML
            return;
        }

        this.xmlFilePath = xmlFilePath;
        
        Bibliotheque library = XmlUtils.buildLibraryFromXML(xmlFilePath);
        
        tableView.getItems().clear();
        tableView.setItems(FXCollections.observableArrayList(library.getLivres()));
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

    @FXML
    private void handleUnloadFile() {
        tableView.getItems().clear();
    }

    @FXML
    private void handleSave() {
        saveData(xmlFilePath);
    }
    
    @FXML
    private void handleSaveAs() {
        String filePath = chooseSaveLocation();
        if (filePath != null) {
            saveData(filePath);
            xmlFilePath = filePath;
        }
    }

    private void saveData(String filePath) {
        XmlUtils.saveLibraryToXml(tableView.getItems(), filePath);
    }

    private String chooseSaveLocation() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save XML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
        File file = fileChooser.showSaveDialog(getStage());
        return file != null ? file.getAbsolutePath() : null;
    }
    
    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }
}
