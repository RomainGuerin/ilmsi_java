package com.e3in.java;

import com.e3in.java.dao.BibliothequeDAO;
import com.e3in.java.dao.DAOManager;
import com.e3in.java.dao.SQLiteDAO;
import com.e3in.java.dao.UserDAO;

public class AppConfig {
    public static DAOManager createDAOManager() {
        SQLiteDAO sqliteDAO = new SQLiteDAO();
        return DAOManager.getInstance(sqliteDAO);
    }

    public static UserDAO createUserDAO() {
        return new UserDAO(createDAOManager());
    }

    public static BibliothequeDAO createBibliothequeDAO() {
        return new BibliothequeDAO(createDAOManager());
    }
}
