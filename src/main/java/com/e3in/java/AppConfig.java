package com.e3in.java;

import com.e3in.java.dao.*;
import com.e3in.java.utils.Common;

import java.io.File;
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
        XmlDAO xmlDAO = new XmlDAO(Common.createOrGetFile(XML_NAME, XML_PATH, AppConfig.class).toString());
        return DAOManager.getInstance(sqliteDAO, xmlDAO);
    }

    /**
     * Retourne une instance de UserDAO.
     *
     * @return Une instance de UserDAO.
     */
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
