package com.e3in.java.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * La classe DAOManager implémente le pattern Singleton pour gérer deux types de DAO : SQLite et XML.
 * Elle permet de basculer entre ces deux DAO en fonction de l'état de connexion (en ligne ou hors ligne).
 */
public class DAOManager implements DAO {
    private static DAOManager instance = null;
    private final DAO sqliteDAO;
    private final DAO xmlDAO;
    private DAO actualDAO;
    private boolean isOnline;

    /**
     * Constructeur privé pour implémenter le pattern Singleton.
     * @param sqliteDAO Instance de DAO pour SQLite.
     * @param xmlDAO Instance de DAO pour XML.
     */
    private DAOManager(DAO sqliteDAO, DAO xmlDAO) {
        this.sqliteDAO = sqliteDAO;
        this.xmlDAO = xmlDAO;
        this.isOnline = true;
        this.actualDAO = this.sqliteDAO;
    }

    /**
     * Méthode pour obtenir l'instance de l'objet XML DAO.
     * @return L'instance de DAO pour XML.
     */
    public DAO getXmlDAO() {
        return this.xmlDAO;
    }

    /**
     * Méthode pour obtenir l'instance unique de DAOManager (singleton).
     * @return L'instance unique de DAOManager.
     */
    public static synchronized DAOManager getInstance() {
        if (instance == null) {
            instance = new DAOManager(new SQLiteDAO(), new XmlDAO(""));
        }
        return instance;
    }

    /**
     * Surcharge de la méthode getInstance pour accepter des DAO personnalisés.
     * @param sqliteDAO Instance personnalisée de DAO pour SQLite.
     * @param xmlDAO Instance personnalisée de DAO pour XML.
     * @return L'instance unique de DAOManager avec les DAO personnalisés.
     */
    public static synchronized DAOManager getInstance(DAO sqliteDAO, DAO xmlDAO) {
        if (instance == null) {
            instance = new DAOManager(sqliteDAO, xmlDAO);
        }
        return instance;
    }

    /**
     * Méthode select implémentée de l'interface DAO.
     * @param tableName Le nom de la table.
     * @param columnNames Les noms des colonnes à sélectionner.
     * @param whereClause Les conditions de sélection.
     * @return Un HashMap contenant les résultats de la sélection.
     */
    @Override
    public HashMap<String, String> select(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        return actualDAO.select(tableName, columnNames, whereClause);
    }

    /**
     * Méthode selectAll implémentée de l'interface DAO.
     * @param tableName Le nom de la table.
     * @param columnNames Les noms des colonnes à sélectionner.
     * @param whereClause Les conditions de sélection.
     * @return Une ArrayList contenant les résultats de la sélection.
     */
    @Override
    public ArrayList<HashMap<String, String>> selectAll(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        return actualDAO.selectAll(tableName, columnNames, whereClause);
    }

    /**
     * Méthode insert implémentée de l'interface DAO.
     * @param tableName Le nom de la table.
     * @param columnAndValue Les colonnes et leurs valeurs à insérer.
     * @return Un HashMap contenant les résultats de l'insertion.
     */
    @Override
    public HashMap<String, String> insert(String tableName, LinkedHashMap<String, String> columnAndValue) {
        return actualDAO.insert(tableName, columnAndValue);
    }

    /**
     * Méthode update implémentée de l'interface DAO.
     * @param tableName Le nom de la table.
     * @param columnAndValue Les colonnes et leurs valeurs à mettre à jour.
     * @param whereClause Les conditions de mise à jour.
     * @return Un HashMap contenant les résultats de la mise à jour.
     */
    @Override
    public HashMap<String, String> update(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause) {
        return actualDAO.update(tableName, columnAndValue, whereClause);
    }

    /**
     * Méthode delete implémentée de l'interface DAO.
     * @param tableName Le nom de la table.
     * @param whereClause Les conditions de suppression.
     * @return Un HashMap contenant les résultats de la suppression.
     */
    @Override
    public HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause) {
        return actualDAO.delete(tableName, whereClause);
    }

    /**
     * Méthode pour définir le mode de connexion (en ligne ou hors ligne).
     * @param isOnline Vrai si en ligne, faux si hors ligne.
     */
    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
        this.actualDAO = this.isOnline ? this.sqliteDAO : this.xmlDAO;
    }

    public boolean isOnline() {
        return this.isOnline;
    }
}
