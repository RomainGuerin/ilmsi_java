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
    public XMLDAOTest() {
        System.out.println("XMLDAOTest");
        DAOManager manager = DAOManager.getInstance();
        manager.setOnline(false);
        manager.insert("Me", new LinkedHashMap<>());

    }

    public static void insertInXmlWithManager() {
        LinkedHashMap<String, String> objMap = new LinkedHashMap<String, String>();
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

    public static void insertInXmlWithController() {
        DAOManager manager = DAOManager.getInstance(new SQLiteDAO(), new XmlDAO("C:\\Users\\Atlas\\Documents\\temp\\xmlDAO.xml"));
        manager.setOnline(false);
        final BibliothequeController bibliothequeController = new BibliothequeController(AppConfig.getBibliothequeDAO());
        bibliothequeController.addLivreBibliotheque(new Livre("monTitre", new Auteur("Nom", "Prenom"), "MaPresentation", "Ma Jaquette", 2022, 1, 2, false));
    }

    public static void createFile() {
        FileOutputStream file = null;
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

    public static void syncSqlToXml() {
        final BibliothequeController bibliothequeController = new BibliothequeController(AppConfig.getBibliothequeDAO());

        AppConfig.getDAOManager().setOnline(true);
        Bibliotheque bibliothequeSql = bibliothequeController.getAllBibliotheque();

        AppConfig.getDAOManager().setOnline(false);
        bibliothequeController.updateBibliotheque(bibliothequeSql);
    }

    public static void main(String[] args) {
//        createFile();
//        insertInXmlWithManager();
//        insertInXmlWithController();
//        getLibs();
        syncSqlToXml();
    }
}
