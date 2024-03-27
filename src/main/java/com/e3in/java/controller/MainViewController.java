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
 * Contrôleur de la vue principale de gestion de la bibliothèque
 * Cette classe gère l'affichage des livres dans un tableau, permet l'ajout, la modification et la suppression de livres,
 * ainsi que le chargement et la sauvegarde des données depuis/vers un fichier XML.
 */
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
    private TextField textFieldJaquette;
    @FXML
    private TextField textFieldParution;
    @FXML
    private Spinner spinnerColonne;
    @FXML
    private Spinner spinnerRangee;
    @FXML
    private RadioButton radioBorrow;
    @FXML
    private RadioButton radioNoBorrow;
    @FXML
    private Button buttonModify;
    @FXML
    private Button buttonRemove;
    private Livre selectedBook;

    /**
     * Constructeur par défaut de la classe MainViewController.
     */
    public MainViewController() { }

    /**
     * Initialise la vue principale.
     * Configure les événements sur les lignes de la table, la vérification et le formatage des champs.
     */
    @FXML
    public void initialize() {
        buttonRemove.setDisable(true);
        
        // Créer une nouvelle colonne pour l'état de l'emprunt
        TableColumn<Livre, String> empruntColumn = new TableColumn<>("Emprunt");
        empruntColumn.setCellValueFactory(cellData -> {
            Livre livre = cellData.getValue();
            String etatEmprunt = livre.getEmprunte() ? "Emprunté" : "Disponible";
            return new SimpleStringProperty(etatEmprunt);
        });
        tableView.getColumns().add(empruntColumn);

        tableView.setRowFactory(tv -> {
            TableRow<Livre> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                // Un click alors on modifie
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    fillForm(row);
                }
                // Double click alors on supprime
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
                    textFieldParution.setText(newValue.replaceAll("\\D", ""));
                }
                if (!newValue.isEmpty() && Integer.parseInt(newValue) > currentYear) {
                    textFieldParution.setText(String.valueOf(currentYear));
                }
            });
            return row;
        });

        tableView.getColumns().forEach(column -> {
            if (column.getText().equals("Jaquette")) {
                TableColumn<Livre, ImageView> jaquetteColumn = new TableColumn<>("Image Jaquette");
                jaquetteColumn.setCellValueFactory(cellData -> {
                    Livre livre = cellData.getValue();
                    String imageUrl = livre.getJaquette();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        return new SimpleObjectProperty<>(createImageViewFromURL(imageUrl));
                    } else {
                        return null;
                    }
                });
                column.getColumns().add(jaquetteColumn);
                jaquetteColumn.setPrefWidth(150);
                jaquetteColumn.styleProperty().set("-fx-alignment: CENTER;");
            }
            if (column.getText().equals("Présentation")) {
                column.setPrefWidth(250);
            }
            if (column.getText().equals("Parution") || column.getText().equals("Colonne") || column.getText().equals("Rangée")) {
                column.setPrefWidth(75);
            }
        });
    }
    
    // Charge un fichier XML et le transforme en une liste de livres.
    @FXML
    private void handleLoadFile() {
        String xmlFilePath;
        xmlFilePath = chooseFile();
        if (xmlFilePath == null || xmlFilePath.isEmpty()) {
            showErrorAlert("Erreur fichier", "Aucun fichier sélectionné");
            return;
        }

        boolean isValid = Xml.validateXml(xmlFilePath);
        if (!isValid) {
            showErrorAlert("Erreur XML", "XML non valide");
            return;
        }

        this.xmlFilePath = xmlFilePath;

        Bibliotheque library = Xml.buildLibraryFromXML(xmlFilePath);

        tableView.getItems().clear();
        if (library != null) {
            tableView.setItems(FXCollections.observableArrayList(library.getLivres()));
        }
    }

    // Affiche une fenêtre d'informations sur l'application.
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
            System.err.println("Erreur de chargement de la page About: " + e.getMessage());
        }
    }

    // Ferme l'application.
    @FXML
    private void handleQuitApp() {
        getStage().close();
    }

    // Gère l'ajout ou la modification d'un livre.
    @FXML
    private void handleModify() {
        if (this.buttonModify.getText().equals("Modifier")) {
            modifyClick();
        } else if (this.buttonModify.getText().equals("Ajouter")) {
            addClick();
        }
    }

    // Quitte la ligne sélectionnée et vide le formulaire.
    @FXML
    private void handleCancel() {
        this.selectedBook = null;
        clearField();
    }

    // Supprime un livre de la table.
    @FXML
    private void handleRemove() {
        if (selectedBook != null) {
            tableView.getItems().remove(selectedBook);
            clearField();
        }
    }

    // Vide la table et le fichier XML actuel.
    @FXML
    private void handleUnloadFile() {
        tableView.getItems().clear();
        selectedBook = null;
        clearField();
    }

    // Sauvegarde les données dans le fichier XML actuel.
    @FXML
    private void handleSave() {
        saveData(xmlFilePath);
    }

    // Sauvegarde les données dans un nouveau fichier XML.
    @FXML
    private void handleSaveAs() {
        String filePath = chooseSaveLocation();
        if (filePath != null) {
            saveData(filePath);
            xmlFilePath = filePath;
        }
    }

    // Exporter les données vers un word (using Apache POI)
    @FXML
    private void handleExport() throws IllegalArgumentException {
        try {
            String path = chooseSaveLocation(getStage());
//            String path = "C:\\Users\\Atlas\\Documents\\Et. cas Medtra - Informations initiales.docx";
            if (path == null) {
                throw new IllegalArgumentException("Aucun emplacement de sauvegarde sélectionné");
            }

            WordController word = new WordController(path);
            word.addHeader();
            word.addFooter();
            word.addCoverPage();
            word.addTableOfContent();
            word.addBooks(this.tableView.getItems());
            word.addBorrowedBooks(this.tableView.getItems());
            word.saveDocument();

        } catch (Exception e) {
            showErrorAlert("Erreur Export", "Erreur lors de l'exportation des données : " + e.getMessage());
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public static String chooseSaveLocation(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un emplacement de sauvegarde");
        
        // Création du filtre pour les fichiers .docx
        ExtensionFilter docxFilter = new ExtensionFilter("Fichiers DOCX (*.docx)", "*.docx");
        fileChooser.getExtensionFilters().add(docxFilter);

        // Affichage de la boîte de dialogue de sélection de fichiers
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            return file.getAbsolutePath();
        }
        return null;
    }

    /**
     * Supprime une ligne de la table.
     * 
     * @param row La ligne à supprimer.
     */
    private void removeTabRow(TableRow<Livre> row) {
        Livre livre = row.getItem();
        if (livre != null) {
            tableView.getItems().remove(livre);
            clearField();
        }
    }

    /**
     * Remplit le formulaire avec les données de la ligne sélectionnée.
     * 
     * @param row Données de la ligne sélectionnée.
     */
    private void fillForm(TableRow<Livre> row) {
        Livre livre = row.getItem();
        this.textFieldTitre.setText(livre.getTitre());
        this.textFieldAuteur.setText(livre.getAuteur().toString());
        this.textFieldPresentation.setText(livre.getPresentation());
        this.textFieldJaquette.setText(livre.getJaquette());
        this.textFieldParution.setText(String.valueOf(livre.getParution()));
        this.spinnerColonne.getValueFactory().setValue(livre.getColonne());
        this.spinnerRangee.getValueFactory().setValue(livre.getRangee());
        setBorrowRadio(livre.getEmprunte());
        System.out.println(livre);
        this.selectedBook = livre;
        this.buttonModify.setText("Modifier");
        this.buttonRemove.setDisable(false);
    }

    // Méthode pour modifier un livre
    private void modifyClick() {
        if (selectedBook != null) {
            try {
                String titre = textFieldTitre.getText().strip();
                String auteur = textFieldAuteur.getText().strip();
                int parution = Integer.parseInt(textFieldParution.getText().strip());

                if (!estLivreUnique(titre, auteur, parution, tableView.getItems())) {
                    showErrorAlert("Erreur Unicité", "Un livre avec le meme auteur/titre/parution existe.");
                    return;
                }

                this.selectedBook.setTitre(textFieldTitre.getText().strip());
                this.selectedBook.setAuteur(textFieldAuteur.getText().strip());
                this.selectedBook.setPresentation(textFieldPresentation.getText().strip());
                this.selectedBook.setJaquette(textFieldJaquette.getText().strip());
                this.selectedBook.setParution(Integer.parseInt(textFieldParution.getText().strip()));
                if (spinnerColonne.getValue() instanceof Integer && spinnerRangee.getValue() instanceof Integer) {
                    this.selectedBook.setColonne((int) spinnerColonne.getValue());
                    this.selectedBook.setRangee((int) spinnerRangee.getValue());
                }
                this.selectedBook.setEmprunte(getBorrowRadio());
                tableView.refresh();
                clearField();
            } catch (Exception e) {
                showErrorAlert("Erreur modification", "Erreur lors de la modification du livre : " + e.getMessage());
            }
        }
    }

    private void setBorrowRadio(boolean emprunte) {
        this.radioBorrow.setSelected(emprunte);
        this.radioNoBorrow.setSelected(!emprunte);
    }
    private boolean getBorrowRadio() {
        return this.radioBorrow.isSelected();
    }

    // Méthode pour ajouter un livre
    private void addClick() {
        try {
            Livre livre = getUserBook();

            if (!estLivreUnique(livre.getTitre(), livre.getAuteur().toString(), livre.getParution(),
                    tableView.getItems())) {
                showErrorAlert("Erreur Unicité", "Un livre avec le meme auteur/titre/parution existe.");
                return;
            }

            tableView.getItems().add(livre);
            tableView.refresh();

            clearField();
        } catch (Exception e) {
            showErrorAlert("Erreur Ajout", "Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }

    private Livre getUserBook() {
        Livre livre = new Livre();
        livre.setTitre(textFieldTitre.getText().strip());
        livre.setAuteur(textFieldAuteur.getText().strip());
        livre.setPresentation(textFieldPresentation.getText().strip());
        livre.setJaquette(textFieldJaquette.getText().strip());
        livre.setParution(Integer.parseInt(textFieldParution.getText().strip()));
        livre.setColonne((int) spinnerColonne.getValue());
        livre.setRangee((int) spinnerRangee.getValue());
        livre.setEmprunte(getBorrowRadio());
        return livre;
    }

    // Méthode pour vérifier si un livre est unique
    private boolean estLivreUnique(String titre, String auteur, int parution, List<Livre> listeLivres) {
        for (Livre livre : listeLivres) {
            if(livre == this.selectedBook) {
                continue;
            }
            if (livre.getTitre().equals(titre) && livre.getAuteur().toString().equals(auteur)
                    && livre.getParution() == parution) {
                return false;
            }
        }
        return true;
    }

    // Méthode pour vider les champs du formulaire
    private void clearField() {
        this.textFieldTitre.setText("");
        this.textFieldAuteur.setText("");
        this.textFieldPresentation.setText("");
        this.textFieldJaquette.setText("");
        this.textFieldParution.setText("");
        this.spinnerColonne.getValueFactory().setValue(0);
        this.spinnerRangee.getValueFactory().setValue(1);
        this.buttonModify.setText("Ajouter");
        buttonRemove.setDisable(true);
    }

    // Méthode pour choisir un fichier XML
    private String chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        String extension = "*.xml";
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("XML files (" + extension + ")", extension);
        fileChooser.getExtensionFilters().add(filter);

        File selectedFile = fileChooser.showOpenDialog(getStage());

        return selectedFile == null ? null : selectedFile.getAbsolutePath();
    }

    // Méthode pour sauvegarder les données dans un fichier XML
    private void saveData(String filePath) {
        Xml.saveLibraryToXml(tableView.getItems(), filePath);
    }

    // Méthode pour choisir l'emplacement de sauvegarde
    private String chooseSaveLocation() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save XML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
        File file = fileChooser.showSaveDialog(getStage());
        return file != null ? file.getAbsolutePath() : null;
    }

    // Méthode pour créer une image à partir d'une URL
    private ImageView createImageViewFromURL(String imageUrl) {
        ImageView imageView = new ImageView();
        try {
            // on vérifie que la jaquette soit un fichier ou un url pour la charger correctement
            if (imageUrl.startsWith("http")) {
                Image image = new Image(imageUrl);
                imageView.setImage(image);
            } else {
                File file = new File(imageUrl);
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            }

            imageView.setFitWidth(125);
            imageView.setFitHeight(125);

            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);

            if (imageView.getImage().isError()) {
                throw new IllegalArgumentException("L'image n'a pas pu être chargée");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
        return imageView;
    }

    /** 
     * Méthode pour afficher une alerte d'erreur
     * 
     * @param title Le titre de l'alerte.
     * @param content Le contenu de l'alerte.
     */
    protected static void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image icon = new Image(Objects.requireNonNull(MainViewController.class.getResourceAsStream("/alert.png")));
        stage.getIcons().add(icon);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Méthode pour récupérer la fenêtre principale
    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }
}
