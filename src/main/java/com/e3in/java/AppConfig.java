package com.e3in.java;

import com.e3in.java.dao.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

public class AppConfig {
    private static final String XML_NAME = "xmlDAO.xml";
    private static final String XML_PATH = System.getProperty("user.home") + File.separator + XML_NAME;

    static Logger logger = Logger.getLogger(AppConfig.class.getName());

    private AppConfig () {}

    public static DAOManager getDAOManager() {
        SQLiteDAO sqliteDAO = new SQLiteDAO();
        XmlDAO xmlDAO = getXmlDAO();
        return DAOManager.getInstance(sqliteDAO, xmlDAO);
    }

    private static XmlDAO getXmlDAO() {
        File xmlFile = new File(XML_PATH);
        if (!xmlFile.exists()) {
            try (InputStream xmlInput = AppConfig.class.getResourceAsStream("/" + XML_NAME)) {
                if (xmlInput == null) {
                    throw new IllegalArgumentException("Fichier xmlDAO.xml introuvable dans les ressources");
                }
                Files.copy(xmlInput, xmlFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la lecture du fichier xmlDAO.xml", e);
            }
        }
        return new XmlDAO(xmlFile.toString());
    }

    public static UserDAO getUserDAO() {
        return new UserDAO(getDAOManager());
    }

    public static BibliothequeDAO getBibliothequeDAO() {
        return new BibliothequeDAO(getDAOManager());
    }
}
