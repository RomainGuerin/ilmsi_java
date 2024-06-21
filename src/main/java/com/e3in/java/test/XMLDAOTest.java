package com.e3in.java.test;

import com.e3in.java.AppConfig;
import com.e3in.java.controller.BibliothequeController;
import com.e3in.java.dao.DAOManager;
import com.e3in.java.dao.SQLiteDAO;
import com.e3in.java.dao.XmlDAO;
import com.e3in.java.model.Auteur;
import com.e3in.java.model.Bibliotheque;
import com.e3in.java.model.Livre;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

public class XMLDAOTest {
    /**
     * Constructeur de la classe de test
     */
    public XMLDAOTest() {
        System.out.println("XMLDAOTest");
        DAOManager manager = DAOManager.getInstance();
        manager.setOnline(false);
        manager.insert("Me", new LinkedHashMap<>());
    }

    /**
     * Méthode pour insérer des données dans un fichier XML en utilisant DAOManager
     */
    public static void insertInXmlWithManager() {
        LinkedHashMap<String, String> objMap = new LinkedHashMap<>();
        objMap.put("Name", "Suzuki");
        objMap.put("Power", "220");
        objMap.put("Type", "2-wheeler");
        objMap.put("Price", "85000");
        System.out.println("Elements of the Map:");
        System.out.println("main dao test");
        DAOManager manager = DAOManager.getInstance(new SQLiteDAO(), new XmlDAO("C:\\Users\\Atlas\\Documents\\temp\\xmlDAO.xml"));
        manager.setOnline(false);
        manager.insert("Me", objMap);
    }

    /**
     * Méthode pour insérer un livre dans une bibliothèque en utilisant BibliothequeController
     */
    public static void insertInXmlWithController() {
        DAOManager manager = DAOManager.getInstance(new SQLiteDAO(), new XmlDAO("C:\\Users\\Atlas\\Documents\\temp\\xmlDAO.xml"));
        manager.setOnline(false);
        final BibliothequeController bibliothequeController = new BibliothequeController(AppConfig.getBibliothequeDAO());
        bibliothequeController.addLivreBibliotheque(new Livre("monTitre", new Auteur("Nom", "Prenom"), "MaPresentation", "Ma Jaquette", 2022, 1, 2, false));
    }

    /**
     * Méthode pour créer un fichier XML avec un contenu par défaut
     */
    public static void createFile() {
        FileOutputStream file;
        try {
            file = new FileOutputStream("C:\\Users\\Atlas\\Documents\\temp\\xmlDAO.xml");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Writer w = new BufferedWriter(new OutputStreamWriter(file, StandardCharsets.UTF_8));
        String defaultText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><bibliotheque></bibliotheque>";
        try {
            w.write(defaultText);
            w.flush();
            w.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Méthode pour récupérer les livres de la bibliothèque en mode hors ligne et en ligne
     */
    public static void getLibs() {
        DAOManager manager = DAOManager.getInstance(new SQLiteDAO(), new XmlDAO("C:\\Users\\Atlas\\Documents\\temp\\xmlDAO.xml"));

        System.out.println("Hors Ligne");
        manager.setOnline(false);
        final BibliothequeController bibliothequeController = new BibliothequeController(AppConfig.getBibliothequeDAO());
        System.out.println(bibliothequeController.getAllBibliotheque().getLivres());

        System.out.println("En Ligne");
        manager.setOnline(true);
        System.out.println(bibliothequeController.getAllBibliotheque().getLivres());
    }

    /**
     * Méthode pour synchroniser les données de la base de données SQL vers le fichier XML
     */
    public static void syncSqlToXml() {
        final BibliothequeController bibliothequeController = new BibliothequeController(AppConfig.getBibliothequeDAO());

        AppConfig.getDAOManager().setOnline(true);
        Bibliotheque bibliothequeSql = bibliothequeController.getAllBibliotheque();

        AppConfig.getDAOManager().setOnline(false);
        bibliothequeController.updateBibliotheque(bibliothequeSql);
    }

    /**
     * Méthode principale pour exécuter les tests
     * @param args les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        // Décommentez les lignes ci-dessous pour exécuter les différentes méthodes de test
        // createFile();
        // insertInXmlWithManager();
        // insertInXmlWithController();
        // getLibs();
        syncSqlToXml();
    }
}
