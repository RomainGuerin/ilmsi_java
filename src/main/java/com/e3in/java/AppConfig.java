package com.e3in.java;

import com.e3in.java.dao.DAOManager;
import com.e3in.java.dao.SQLiteDAO;
import com.e3in.java.dao.XMLDAO;
import com.e3in.java.dao.UserDAO;
import com.e3in.java.utils.SQLiteConnection;

public class AppConfig {
    public static DAOManager createDAOManager() {
        SQLiteDAO sqliteDAO = new SQLiteDAO();
        XMLDAO xmlDAO = new XMLDAO(Main.class.getResource("/monXML.xml").getPath());
        return DAOManager.getInstance(sqliteDAO, xmlDAO);
    }

    public static UserDAO createUserDAO() {
        return new UserDAO(createDAOManager());
    }
}
