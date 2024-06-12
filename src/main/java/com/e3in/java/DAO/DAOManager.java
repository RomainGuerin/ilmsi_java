package com.e3in.java.dao;

import com.e3in.java.utils.SQLiteConnection;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
public class DAOManager implements DAO {
    private static DAOManager instance = null;
    private final DAO sqliteDAO;
    private final DAO xmlDAO;

    private DAOManager(DAO sqliteDAO, DAO xmlDAO) {
        this.sqliteDAO = sqliteDAO;
        this.xmlDAO = xmlDAO;
    }

    public static synchronized DAOManager getInstance() {
        if (instance == null) {
            instance = new DAOManager(new SQLiteDAO(), new XMLDAO("DefaultDB"));
        }
        return instance;
    }

    public static synchronized DAOManager getInstance(DAO sqliteDAO, DAO xmlDAO) {
        if (instance == null) {
            instance = new DAOManager(sqliteDAO, xmlDAO);
        }
        return instance;
    }

    @Override
    public HashMap<String, String> select(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        HashMap<String, String> result = sqliteDAO.select(tableName, columnNames, whereClause);
        if (result.isEmpty()) {
            result = xmlDAO.select(tableName, columnNames, whereClause);
        }
        return result;
    }

    @Override
    public HashMap<String, String> insert(String tableName, HashMap<String, String> columnAndValue) {
        HashMap<String, String> result = sqliteDAO.insert(tableName, columnAndValue);
        if ("success".equals(result.get("status"))) {
            xmlDAO.insert(tableName, columnAndValue);
        }
        return result;
    }

    @Override
    public HashMap<String, String> update(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause) {
        HashMap<String, String> result = sqliteDAO.update(tableName, columnAndValue, whereClause);
        if ("success".equals(result.get("status"))) {
            xmlDAO.update(tableName, columnAndValue, whereClause);
        }
        return result;
    }

    @Override
    public HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause) {
        HashMap<String, String> result = sqliteDAO.delete(tableName, whereClause);
        if ("success".equals(result.get("status"))) {
            xmlDAO.delete(tableName, whereClause);
        }
        return result;
    }
}
