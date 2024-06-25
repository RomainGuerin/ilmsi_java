package com.e3in.java;

import com.e3in.java.dao.*;
import com.e3in.java.utils.Common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

/**
 * Classe de configuration de l'application.
 * Cette classe fournit des méthodes pour obtenir des instances de DAO (Data Access Objects).
 */
public class AppConfig {
    private static final String XML_NAME = "xmlDAO.xml";
    private static final String XML_PATH = System.getProperty("user.home") + File.separator + XML_NAME;

    static Logger logger = Logger.getLogger(AppConfig.class.getName());

    // Constructeur privé pour empêcher l'instanciation de cette classe utilitaire
    private AppConfig() {}

    /**
     * Retourne une instance de DAOManager configurée avec des DAO SQLite et XML.
     *
     * @return Une instance de DAOManager.
     */
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

    /**
     * Retourne une instance de BibliothequeDAO.
     *
     * @return Une instance de BibliothequeDAO.
     */
    public static BibliothequeDAO getBibliothequeDAO() {
        return new BibliothequeDAO(getDAOManager());
    }
}
