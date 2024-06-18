package com.e3in.java;

import com.e3in.java.dao.*;

public class AppConfig {

    private AppConfig () {}

    public static DAOManager getDAOManager() {
        SQLiteDAO sqliteDAO = new SQLiteDAO();
        XmlDAO xmlDAO = new XmlDAO("%USERPROFILE%\\temp\\xmlDAO.xml");
        return DAOManager.getInstance(sqliteDAO, xmlDAO);
    }

    public static UserDAO getUserDAO() {
        return new UserDAO(getDAOManager());
    }

    public static BibliothequeDAO getBibliothequeDAO() {
        return new BibliothequeDAO(getDAOManager());
    }
}
