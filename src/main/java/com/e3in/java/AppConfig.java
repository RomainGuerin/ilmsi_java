package com.e3in.java;

import com.e3in.java.dao.*;

/**
 * Classe de configuration de l'application.
 * Cette classe fournit des méthodes pour obtenir des instances de DAO (Data Access Objects).
 */
public class AppConfig {

    // Constructeur privé pour empêcher l'instanciation de cette classe utilitaire
    private AppConfig() {}

    /**
     * Retourne une instance de DAOManager configurée avec des DAO SQLite et XML.
     *
     * @return Une instance de DAOManager.
     */
    public static DAOManager getDAOManager() {
        SQLiteDAO sqliteDAO = new SQLiteDAO();
        XmlDAO xmlDAO = new XmlDAO(System.getProperty("user.home") + "\\temp\\xmlDAO.xml");
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
