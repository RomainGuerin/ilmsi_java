package com.e3in.java.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DAOManager implements DAO {
    private static DAOManager instance = null;
    private final DAO sqliteDAO;

    private DAOManager(DAO sqliteDAO) {
        this.sqliteDAO = sqliteDAO;
    }

    public static synchronized DAOManager getInstance() {
        if (instance == null) {
            instance = new DAOManager(new SQLiteDAO());
        }
        return instance;
    }

    public static synchronized DAOManager getInstance(DAO sqliteDAO) {
        if (instance == null) {
            instance = new DAOManager(sqliteDAO);
        }
        return instance;
    }

    @Override
    public HashMap<String, String> select(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        return sqliteDAO.select(tableName, columnNames, whereClause);
    }

    @Override
    public ArrayList<HashMap<String, String>> selectAll(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        return sqliteDAO.selectAll(tableName, columnNames, whereClause);
    }

    @Override
    public HashMap<String, String> insert(String tableName, HashMap<String, String> columnAndValue) {
        return sqliteDAO.insert(tableName, columnAndValue);
    }

    @Override
    public HashMap<String, String> update(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause) {
        return sqliteDAO.update(tableName, columnAndValue, whereClause);
    }

    @Override
    public HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause) {
        return sqliteDAO.delete(tableName, whereClause);
    }
}
