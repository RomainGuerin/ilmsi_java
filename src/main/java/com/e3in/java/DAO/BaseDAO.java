package com.e3in.java.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public abstract class BaseDAO implements DAO {
    protected Connection connection;

    protected void beginTransaction() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.setAutoCommit(false);
        }
    }

    protected void commitTransaction() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit();
            connection.setAutoCommit(true);
        }
    }

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

    @Override
    public abstract HashMap<String, String> get(String tableName, List<String> columnNames, HashMap<String, String> whereClause);

    @Override
    public abstract HashMap<String, String> insert(String tableName, HashMap<String, String> columnAndValue);

    @Override
    public abstract HashMap<String, String> update(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause);

    @Override
    public abstract HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause);
}
