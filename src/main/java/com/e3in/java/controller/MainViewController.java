package com.e3in.java.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.function.UnaryOperator;

import com.e3in.java.model.Bibliotheque;
import com.e3in.java.model.Livre;

public class MainViewController {

    private String xmlFilePath = "";
    
    @FXML
    private TableView<Livre> tableView;
    @FXML
    private TextField textFieldTitre;
    @FXML
    private TextField textFieldAuteur;
    @FXML
    private TextField textFieldPresentation;
    @FXML
    private TextField textFieldParution;
    @FXML
    private Spinner spinnerColonne;
    @FXML
    private Spinner spinnerRangee;
    @FXML
    private Button buttonModify;
    private Livre selectedBook;
    public MainViewController() { }

    @FXML
    public void initialize() {
        tableView.setRowFactory(tv -> {
            TableRow<Livre> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    fillForm(row);
                }
                else if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    removeTabRow(row);
                }
            });

            UnaryOperator<TextFormatter.Change> filter = change -> {
                if (change.getControlNewText().isEmpty() || change.getControlNewText().matches("\\d+")) {
                    return change;
                }
                return null;
            };

            TextFormatter<String> textFormatter = new TextFormatter<>(filter);

            textFieldParution.setTextFormatter(textFormatter);

            int currentYear = LocalDate.now().getYear();
            textFieldParution.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    textFieldParution.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (!newValue.isEmpty() && Integer.parseInt(newValue) > currentYear) {
                    textFieldParution.setText(String.valueOf(currentYear));
                }
            });

            return row;
        });
    }

    private void removeTabRow(TableRow<Livre> row) {
        Livre livre = row.getItem();
        if (livre != null) {
            tableView.getItems().remove(livre);
        }
    }

    private void fillForm(TableRow<Livre> row) {
        Livre livre = row.getItem();
        this.textFieldTitre.setText(livre.getTitre());
        this.textFieldAuteur.setText(livre.getAuteur().toString());
        this.textFieldPresentation.setText(livre.getPresentation());
        this.textFieldParution.setText(String.valueOf(livre.getParution()));
        this.spinnerColonne.getValueFactory().setValue(livre.getColonne());
        this.spinnerRangee.getValueFactory().setValue(livre.getRangee());
        System.out.println(livre);
        this.selectedBook = livre;
        this.buttonModify.setText("Modifier");
    }
    @FXML
    private void handleLoadFile() {
        String xmlFilePath;
        xmlFilePath = chooseFile();
        if (xmlFilePath == null || xmlFilePath.isEmpty()) {
            showErrorAlert("Erreur", "Aucun fichier sélectionné");
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
        if (library != null) {
            tableView.setItems(FXCollections.observableArrayList(library.getLivres()));
        }
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

    @FXML
    private void handleModify() {
        if (this.buttonModify.getText().equals("Modifier")) {
            modifyClick();
        } else if (this.buttonModify.getText().equals("Ajouter")) {
            addClick();
        }
    }
    private boolean estLivreUnique(String titre, String auteur, int parution, List<Livre> listeLivres) {
        for (Livre livre : listeLivres) {
            if (livre.getTitre().equals(titre) && livre.getAuteur().toString().equals(auteur) && livre.getParution() == parution) {
                return false;
            }
        }
        return true;
    }
    private void modifyClick(){
        if(selectedBook != null) {
            String titre = textFieldTitre.getText().strip();
            String auteur = textFieldAuteur.getText().strip();
            int parution = Integer.parseInt(textFieldParution.getText().strip());

            if (!estLivreUnique(titre, auteur, parution, tableView.getItems())) {
                showErrorAlert("Erreur", "Un livre avec le meme auteur/titre/parution existe.");
                return;
            }

            System.out.println("spin = " + spinnerColonne.getValue() + spinnerColonne.getPromptText());
            System.out.println("spin2 = " +  spinnerColonne.getValue().getClass() + spinnerColonne.getPromptText().getClass());
            this.selectedBook.setTitre(textFieldTitre.getText().strip());
            this.selectedBook.setAuteur(textFieldAuteur.getText().strip());
            this.selectedBook.setPresentation(textFieldPresentation.getText().strip());
            this.selectedBook.setParution(Integer.parseInt(textFieldParution.getText().strip()));
            if(spinnerColonne.getValue() instanceof Integer && spinnerRangee.getValue() instanceof Integer) {
                this.selectedBook.setColonne((int) spinnerColonne.getValue());
                this.selectedBook.setRangee((int) spinnerRangee.getValue());
            }
            tableView.refresh();
            clearField();
        }
    }
    private void addClick(){
        Livre livre = new Livre();
        livre.setTitre(textFieldTitre.getText().strip());
        livre.setAuteur(textFieldAuteur.getText().strip());
        livre.setPresentation(textFieldPresentation.getText().strip());
        livre.setParution(Integer.parseInt(textFieldParution.getText().strip()));
        livre.setColonne(Integer.parseInt((String) spinnerColonne.getValue()));
        livre.setRangee(Integer.parseInt((String) spinnerRangee.getValue()));

        tableView.getItems().add(livre);
        tableView.refresh();
    }

    @FXML
    private void handleCancel() {
        this.selectedBook = null;
        clearField();
    }

    private void clearField() {
        this.textFieldTitre.setText("");
        this.textFieldAuteur.setText("");
        this.textFieldPresentation.setText("");
        this.textFieldParution.setText("");
        this.spinnerColonne.getValueFactory().setValue(0);
        this.spinnerRangee.getValueFactory().setValue(1);
        this.buttonModify.setText("Ajouter");
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
        selectedBook = null;
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

    protected static void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
