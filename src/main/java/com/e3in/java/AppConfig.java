package com.e3in.java;

import com.e3in.java.dao.*;
import com.e3in.java.utils.Common;

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
        XmlDAO xmlDAO = new XmlDAO(Common.createOrGetFile(XML_NAME, XML_PATH, AppConfig.class).toString());
        return DAOManager.getInstance(sqliteDAO, xmlDAO);
    }

    public static UserDAO getUserDAO() {
        return new UserDAO(getDAOManager());
    }

    public static BibliothequeDAO getBibliothequeDAO() {
        return new BibliothequeDAO(getDAOManager());
    }
}
