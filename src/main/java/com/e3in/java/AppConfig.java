package com.e3in.java;

import com.e3in.java.dao.*;

import java.util.Objects;

public class AppConfig {

    private AppConfig () {}

    public static DAOManager getDAOManager() {
        SQLiteDAO sqliteDAO = new SQLiteDAO();
        String xmlPath = Objects.requireNonNull(AppConfig.class.getResource("/xmlDAO.xml")).getPath();
        XmlDAO xmlDAO = new XmlDAO(xmlPath);
        return DAOManager.getInstance(sqliteDAO, xmlDAO);
    }

    public static UserDAO getUserDAO() {
        return new UserDAO(getDAOManager());
    }

    public static BibliothequeDAO getBibliothequeDAO() {
        return new BibliothequeDAO(getDAOManager());
    }
}
