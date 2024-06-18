package com.e3in.java.controller;

import com.e3in.java.AppConfig;
import com.e3in.java.model.Bibliotheque;
import com.e3in.java.model.Livre;
import com.e3in.java.model.User;
import com.e3in.java.utils.Common;
import com.e3in.java.utils.UserAwareController;
import com.e3in.java.utils.Xml;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Contrôleur de la vue principale de gestion de la bibliothèque
 * Cette classe gère l'affichage des livres dans un tableau, permet l'ajout, la modification et la suppression de livres,
 * ainsi que le chargement et la sauvegarde des données depuis/vers un fichier XML.
 */
public class MainViewController implements UserAwareController {

    private final BibliothequeController bibliothequeController = new BibliothequeController(AppConfig.createBibliothequeDAO());

    private User connectedUser;

    private boolean isOnline = false;

    private String xmlFilePath = "";

    private Livre selectedBook;

    @FXML
    private Menu xmlMenu;
    @FXML
    private Menu bddMenu;
    @FXML
    private MenuItem connectionBDD;
    @FXML
    private Menu editionMenu;

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
    private HBox containerEditionLivre;
    @FXML
    private Button buttonModify;
    @FXML
    private Button buttonRemove;

    @FXML
    private Label userTypeChip;
    @FXML
    private Label connectionTypeChip;
    @FXML
    private Label lastEditDateChip;

    /**
     * Constructeur par défaut de la classe MainViewController.
     */
    public MainViewController() {}

    /**
     * Initialise la vue principale.
     * Configure les événements sur les lignes de la table, la vérification et le formatage des champs.
     */
    @FXML
    public void initialize() {
        buttonRemove.setDisable(true);

        configureTableRows();
        configureTableColumns();
        configureTableColumnEmprunt();
    }

    private void configureTableRows() {
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

            configureTextFieldParution();
            return row;
        });
    }

    private void configureTextFieldParution() {
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
    }

    private void configureTableColumns() {
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

    private void configureTableColumnEmprunt() {
        // Créer une nouvelle colonne pour l'état de l'emprunt
        TableColumn<Livre, String> empruntColumn = new TableColumn<>("Emprunt");
        empruntColumn.setCellValueFactory(cellData -> {
            Livre livre = cellData.getValue();
            String etatEmprunt = livre.getEmprunte() ? "Emprunté" : "Disponible";
            return new SimpleStringProperty(etatEmprunt);
        });
        tableView.getColumns().add(empruntColumn);
    }

    @Override
    public void setUser(User user) {
        connectedUser = user;
        updateStatusBox();
        allowEditOnTable(connectedUser.isAdmin());
        if (!connectedUser.isAdmin()) {
            handleConnectionBDD();
        }
    }

    private void updateStatusBox() {
        if (connectedUser != null) {
            userTypeChip.setText(connectedUser.isAdmin() ? "Admin" : "User");
            userTypeChip.getStyleClass().setAll(connectedUser.isAdmin() ? "chip-admin" : "chip");
        }
        connectionTypeChip.setText("Status : " + (isOnline ? "En ligne" : "Hors ligne"));
        lastEditDateChip.setText("Dernière mise à jour : " + Common.getCurrentDateTime());
    }

    private void allowEditOnTable(boolean isAdmin) {
        this.textFieldTitre.setDisable(!isAdmin);
        this.textFieldAuteur.setDisable(!isAdmin);
        this.textFieldPresentation.setDisable(!isAdmin);
        this.textFieldJaquette.setDisable(!isAdmin);
        this.textFieldParution.setDisable(!isAdmin);
        this.spinnerColonne.setDisable(!isAdmin);
        this.spinnerRangee.setDisable(!isAdmin);
        this.radioBorrow.setDisable(!isAdmin);
        this.radioNoBorrow.setDisable(!isAdmin);

        containerEditionLivre.setVisible(isAdmin);
        xmlMenu.setVisible(isAdmin);
        editionMenu.setVisible(isAdmin);
    }

    // Charge un fichier XML et le transforme en une liste de livres.
    @FXML
    private void handleLoadFile() {
        handleUnloadFile();

        String xmlFilePath = chooseLocation("open", getStage(), "xml");
        if (xmlFilePath == null || xmlFilePath.isEmpty()) {
            Common.showAlert(Alert.AlertType.ERROR, "Erreur fichier", "Aucun fichier sélectionné");
            return;
        }

        if (!Xml.validateXml(xmlFilePath)) {
            Common.showAlert(Alert.AlertType.ERROR, "Erreur XML", "XML non valide");
            return;
        }

        this.xmlFilePath = xmlFilePath;

        insertLibraryInTableView(Xml.buildLibraryFromXML(xmlFilePath));
    }

    @FXML
    private void handleConnectionBDD() {
        handleUnloadFile();
        connectionBDD.setText("Rafraîchir");
        isOnline = true;
        updateStatusBox();

        insertLibraryInTableView(bibliothequeController.getAllBibliotheque());
    }

    private void insertLibraryInTableView(Bibliotheque library) {
        if (library != null) {
            tableView.setItems(FXCollections.observableArrayList(library.getLivres()));
        } else {
            Common.showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Erreur lors du chargement de la bibliothèque.");
        }
    }

    // Affiche une fenêtre d'informations sur l'application.
    @FXML
    private void handleInfos() {
        Common.showAboutPopup();
    }

    // Ferme l'application.
    @FXML
    private void handleQuitApp() {
        Common.closeApp(getStage());
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
        selectedBook = null;
        clearField();
    }

    // Supprime un livre de la table.
    @FXML
    private void handleRemove() {
        if (selectedBook != null) {
            if (isOnline) {
                if (!bibliothequeController.removeLivreBibliotheque(selectedBook)) {
                    Common.showAlert(Alert.AlertType.ERROR, "Erreur de la suppression", "Impossible de supprimer le livre, merci de ré-essayer");
                    return;
                }
            }
            tableView.getItems().remove(selectedBook);
            clearField();
        }
    }

    // Vide la table et le fichier XML actuel.
    @FXML
    private void handleUnloadFile() {
        connectionBDD.setText("Connexion");

        isOnline = false;
        updateStatusBox();

        selectedBook = null;
        clearField();
        tableView.getItems().clear();

        xmlFilePath = "";
    }

    // Sauvegarde les données dans le fichier XML actuel.
    @FXML
    private void handleSave() {
        saveData(xmlFilePath);
    }

    // Sauvegarde les données dans un nouveau fichier XML.
    @FXML
    private void handleSaveAs() {
        String filePath = chooseLocation("save", getStage(), "xml");
        if (filePath != null) {
            saveData(filePath);
            xmlFilePath = filePath;
        }
    }

    // Exporter les données vers un word (using Apache POI)
    @FXML
    private void handleExport() throws IllegalArgumentException {
        try {
            String path = chooseLocation("save", getStage(), "docx");
            if (path == null) {
                throw new IllegalArgumentException("Aucun emplacement de sauvegarde sélectionné");
            }

            // TODO : Déplacer le chargement du word controller je pense qu'on devrait avoir uniquement le word.addBooks() et le word.addBorrowed books
            WordController word = new WordController(path);
            word.addHeader();
            word.addFooter();
            word.addCoverPage();
            word.addTableOfContent();
            word.addBooks(this.tableView.getItems());
            word.addBorrowedBooks(this.tableView.getItems());
            word.saveDocument();
        } catch (Exception e) {
            Common.showAlert(Alert.AlertType.ERROR, "Erreur Export", "Erreur lors de l'exportation des données : " + e.getMessage());
            System.err.println(e);
            e.printStackTrace();
        }
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
        setFormFields(livre);
        setBorrowRadio(livre.getEmprunte());
        selectedBook = livre;
        buttonModify.setText("Modifier");
        buttonRemove.setDisable(false);
    }

    // Méthode pour vider les champs du formulaire
    private void clearField() {
        setFormFields(null);
        this.radioBorrow.setSelected(false);
        this.radioNoBorrow.setSelected(false);
        buttonModify.setText("Ajouter");
        buttonRemove.setDisable(true);
    }

    private void setFormFields(Livre livre) {
        textFieldTitre.setText(livre != null ? livre.getTitre() : "");
        textFieldAuteur.setText(livre != null ? livre.getAuteur().toString() : "");
        textFieldPresentation.setText(livre != null ? livre.getPresentation() : "");
        textFieldJaquette.setText(livre != null ? livre.getJaquette() : "");
        textFieldParution.setText(livre != null ? String.valueOf(livre.getParution()) : "");
        spinnerColonne.getValueFactory().setValue(livre != null ? livre.getColonne() : 1);
        spinnerRangee.getValueFactory().setValue(livre != null ? livre.getRangee() : 1);
    }

    // Méthode pour modifier un livre
    private void modifyClick() {
        if (selectedBook != null) {
            try {
                String titre = textFieldTitre.getText().strip();
                String auteur = textFieldAuteur.getText().strip();
                int parution = Integer.parseInt(textFieldParution.getText().strip());

                if (!estLivreUnique(titre, auteur, parution, tableView.getItems())) {
                    Common.showAlert(Alert.AlertType.ERROR, "Erreur Unicité", "Un livre avec le meme auteur/titre/parution existe.");
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

                if (isOnline) {
                    if (!bibliothequeController.updateLivreBibliotheque(selectedBook)) {
                        Common.showAlert(Alert.AlertType.ERROR, "Erreur de la mise à jour", "Impossible de mettre à jour le livre, merci de ré-essayer");
                        return;
                    }
                }

                tableView.refresh();
                clearField();
            } catch (Exception e) {
                Common.showAlert(Alert.AlertType.ERROR, "Erreur modification", "Erreur lors de la modification du livre : " + e.getMessage());
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
                Common.showAlert(Alert.AlertType.ERROR, "Erreur Unicité", "Un livre avec le meme auteur/titre/parution existe.");
                return;
            }

            tableView.getItems().add(livre);
            tableView.refresh();

            if (isOnline) {
                if (!bibliothequeController.addLivreBibliotheque(livre)) {
                    throw new Exception("Impossible d'ajouter le livre dans la BDD");
                }

            }

            clearField();
        } catch (Exception e) {
            Common.showAlert(Alert.AlertType.ERROR, "Erreur d'ajout", "Erreur lors de l'ajout du livre : " + e.getMessage());
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
    // TODO : Nom de la method en français
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

    // Méthode pour sauvegarder les données dans un fichier XML
    private void saveData(String filePath) {
        Xml.saveLibraryToXml(tableView.getItems(), filePath);
    }

    // Méthode pour choisir l'emplacement de fichier
    public static String chooseLocation(String typeLocation, Stage primaryStage, String extension) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un emplacement de fichier");

        // Création du filtre pour les fichiers avec l'extension spécifiée
        ExtensionFilter fileFilter = new ExtensionFilter("Fichiers (" + extension + ")", "*." + extension);
        fileChooser.getExtensionFilters().add(fileFilter);

        // Affichage de la boîte de dialogue de sélection de fichiers
        File file;
        if (typeLocation.equals("save")) {
            file = fileChooser.showSaveDialog(primaryStage);
        } else {
            file = fileChooser.showOpenDialog(primaryStage);
        }
        return file != null ? file.getAbsolutePath() : null;
    }

    // Méthode pour créer une image à partir d'une URL ou d'un chemin de fichier
    private ImageView createImageViewFromURL(String imagePath) {
        ImageView imageView = new ImageView();
        String url = imagePath.startsWith("http") ? imagePath : new File(imagePath).toURI().toString();
        Image image = new Image(url, 100, 150, true, true, true);
        image.errorProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                System.err.println("Erreur lors du chargement de l'image : " + image.getException().getMessage());
            }
        });
        imageView.setImage(image);
        imageView.setCache(true);
        return imageView;
    }

    // Méthode pour récupérer la fenêtre principale
    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }
}
