package com.e3in.java.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public abstract class BaseDAO implements DAO {
    protected Connection connection;
    static Logger logger = Logger.getLogger(BaseDAO.class.getName());

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
                logger.severe("Erreur de rollback de la transaction: " + e.getMessage());
            }
        }
    }
}
