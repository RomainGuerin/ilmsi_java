package com.e3in.java.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * La classe abstraite BaseDAO fournit des fonctionnalités de base pour l'interaction avec une base de données.
 * Elle comprend des méthodes pour gérer les transactions et des méthodes abstraites pour les opérations CRUD.
 */
public abstract class BaseDAO implements DAO {
    // Objet Connection pour interagir avec la base de données
    protected Connection connection;

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
                e.printStackTrace();
            }
        }
    }

    /**
     * Méthode abstraite pour sélectionner un seul enregistrement dans la base de données.
     *
     * @param tableName  Le nom de la table.
     * @param columnNames Les noms des colonnes à sélectionner.
     * @param whereClause La clause WHERE pour filtrer les enregistrements.
     * @return Une HashMap contenant les colonnes et leurs valeurs pour l'enregistrement sélectionné.
     */
    @Override
    public abstract HashMap<String, String> select(String tableName, List<String> columnNames, HashMap<String, String> whereClause);

    /**
     * Méthode abstraite pour sélectionner plusieurs enregistrements dans la base de données.
     *
     * @param tableName  Le nom de la table.
     * @param columnNames Les noms des colonnes à sélectionner.
     * @param whereClause La clause WHERE pour filtrer les enregistrements.
     * @return Une ArrayList contenant des HashMap pour chaque enregistrement sélectionné.
     */
    @Override
    public abstract ArrayList<HashMap<String, String>> selectAll(String tableName, List<String> columnNames, HashMap<String, String> whereClause);

    /**
     * Méthode abstraite pour insérer un nouvel enregistrement dans la base de données.
     *
     * @param tableName      Le nom de la table.
     * @param columnAndValue Une LinkedHashMap contenant les colonnes et leurs valeurs pour l'insertion.
     * @return Une HashMap contenant les colonnes et leurs valeurs pour l'enregistrement inséré.
     */
    @Override
    public abstract HashMap<String, String> insert(String tableName, LinkedHashMap<String, String> columnAndValue);

    /**
     * Méthode abstraite pour mettre à jour des enregistrements existants dans la base de données.
     *
     * @param tableName      Le nom de la table.
     * @param columnAndValue Une HashMap contenant les colonnes et leurs nouvelles valeurs.
     * @param whereClause    La clause WHERE pour filtrer les enregistrements à mettre à jour.
     * @return Une HashMap contenant les colonnes et leurs valeurs pour l'enregistrement mis à jour.
     */
    @Override
    public abstract HashMap<String, String> update(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause);

    /**
     * Méthode abstraite pour supprimer des enregistrements dans la base de données.
     *
     * @param tableName   Le nom de la table.
     * @param whereClause La clause WHERE pour filtrer les enregistrements à supprimer.
     * @return Une HashMap contenant les colonnes et leurs valeurs pour l'enregistrement supprimé.
     */
    @Override
    public abstract HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause);
}
