package com.e3in.java.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * La classe abstraite BaseDAO fournit des fonctionnalités de base pour l'interaction avec une base de données.
 * Elle comprend des méthodes pour gérer les transactions et des méthodes abstraites pour les opérations CRUD.
 */
public abstract class BaseDAO implements DAO {
    // Objet Connection pour interagir avec la base de données
    protected Connection connection;

    static Logger logger = Logger.getLogger(BaseDAO.class.getName());

    /**
     * Démarre une transaction en désactivant l'auto-commit.
     *
     * @throws SQLException Si une erreur de SQL survient.
     */
    protected void beginTransaction() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.setAutoCommit(false);
        }
    }

    /**
     * Valide la transaction en cours et réactive l'auto-commit.
     *
     * @throws SQLException Si une erreur de SQL survient.
     */
    protected void commitTransaction() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit();
            connection.setAutoCommit(true);
        }
    }

    /**
     * Annule la transaction en cours et réactive l'auto-commit.
     */
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
