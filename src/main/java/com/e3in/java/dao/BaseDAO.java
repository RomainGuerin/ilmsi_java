package com.e3in.java.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class BaseDAO implements DAO {
    // Connection object to interact with the database
    protected Connection connection;

    // Begins a transaction by setting auto-commit to false
    protected void beginTransaction() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.setAutoCommit(false);
        }
    }

    // Commits the current transaction and resets auto-commit to true
    protected void commitTransaction() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit();
            connection.setAutoCommit(true);
        }
    }

    // Rolls back the current transaction and resets auto-commit to true
    protected void rollbackTransaction() {
        if (connection != null) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Abstract method for selecting a single record from the database
    @Override
    public abstract HashMap<String, String> select(String tableName, List<String> columnNames, HashMap<String, String> whereClause);

    // Abstract method for selecting multiple records from the database
    @Override
    public abstract ArrayList<HashMap<String, String>> selectAll(String tableName, List<String> columnNames, HashMap<String, String> whereClause);

    // Abstract method for inserting a new record into the database
    @Override
    public abstract HashMap<String, String> insert(String tableName, LinkedHashMap<String, String> columnAndValue);

    // Abstract method for updating existing records in the database
    @Override
    public abstract HashMap<String, String> update(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause);

    // Abstract method for deleting records from the database
    @Override
    public abstract HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause);
}
