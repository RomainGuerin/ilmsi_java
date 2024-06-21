package com.e3in.java.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DAOManager implements DAO {
    private static DAOManager instance = null;
    private final DAO sqliteDAO;
    private final DAO xmlDAO;
    private DAO actualDAO;
    private boolean isOnline;

    // Constructeur privé pour implémenter le pattern Singleton
    private DAOManager(DAO sqliteDAO, DAO xmlDAO) {
        this.sqliteDAO = sqliteDAO;
        this.xmlDAO = xmlDAO;
        this.isOnline = true;
        this.actualDAO = this.sqliteDAO;
    }

    // Méthode pour obtenir l'instance de l'objet XML DAO
    public DAO getXmlDAO() {
        return this.xmlDAO;
    }

    // Méthode pour obtenir l'instance unique de DAOManager (singleton)
    public static synchronized DAOManager getInstance() {
        if (instance == null) {
            instance = new DAOManager(new SQLiteDAO(), new XmlDAO());
        }
        return instance;
    }

    // Surcharge de la méthode getInstance pour accepter des DAO personnalisés
    public static synchronized DAOManager getInstance(DAO sqliteDAO, DAO xmlDAO) {
        if (instance == null) {
            instance = new DAOManager(sqliteDAO, xmlDAO);
        }
        return instance;
    }

    // Méthode select implémentée de l'interface DAO
    @Override
    public HashMap<String, String> select(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        return actualDAO.select(tableName, columnNames, whereClause);
    }

    // Méthode selectAll implémentée de l'interface DAO
    @Override
    public ArrayList<HashMap<String, String>> selectAll(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        return actualDAO.selectAll(tableName, columnNames, whereClause);
    }

    // Méthode insert implémentée de l'interface DAO
    @Override
    public HashMap<String, String> insert(String tableName, LinkedHashMap<String, String> columnAndValue) {
        return actualDAO.insert(tableName, columnAndValue);
    }

    // Méthode update implémentée de l'interface DAO
    @Override
    public HashMap<String, String> update(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause) {
        return actualDAO.update(tableName, columnAndValue, whereClause);
    }

    // Méthode delete implémentée de l'interface DAO
    @Override
    public HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause) {
        return actualDAO.delete(tableName, whereClause);
    }

    // Méthode pour définir le mode de connexion (en ligne ou hors ligne)
    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
        this.actualDAO = this.isOnline ? this.sqliteDAO : this.xmlDAO;
    }
}
