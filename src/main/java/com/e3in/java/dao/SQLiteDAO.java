package com.e3in.java.dao;

import com.e3in.java.utils.Constants;
import com.e3in.java.utils.SQLiteConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * La classe SQLiteDAO étend BaseDAO et implémente les opérations de base de données
 * pour une base de données SQLite.
 */
public class SQLiteDAO extends BaseDAO {

    static Logger logger = Logger.getLogger(SQLiteDAO.class.getName());

    public SQLiteDAO() {
        try {
            this.connection = SQLiteConnection.getInstance().getConnection();
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.severe("Erreur de connexion à la base de données SQLite: " + e.getMessage());
        }
    }

    /**
     * Exécute une requête SQL et retourne un seul résultat.
     * @param query La requête SQL à exécuter.
     * @param parameters Les paramètres de la requête.
     * @param columnNames Les colonnes à sélectionner.
     * @return Un HashMap contenant le résultat.
     * @throws SQLException En cas d'erreur SQL.
     */
    private HashMap<String, String> executeQuery(String query, List<Object> parameters, List<String> columnNames) throws SQLException {
        HashMap<String, String> result = new HashMap<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            beginTransaction();
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    for (String column : columnNames) {
                        result.put(column, rs.getString(column));
                    }
                }
            }
            commitTransaction();
        } catch (SQLException e) {
            rollbackTransaction();
            throw e;
        }
        return result;
    }

    /**
     * Exécute une requête SQL et retourne plusieurs résultats.
     * @param query La requête SQL à exécuter.
     * @param parameters Les paramètres de la requête.
     * @param columnNames Les colonnes à sélectionner.
     * @return Une ArrayList contenant les résultats.
     * @throws SQLException En cas d'erreur SQL.
     */
    private ArrayList<HashMap<String, String>> executeMultiResultQuery(String query, List<Object> parameters, List<String> columnNames) throws SQLException {
        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            beginTransaction();
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    HashMap<String, String> row = new HashMap<>();
                    for (String column : columnNames) {
                        row.put(column, rs.getString(column));
                    }
                    results.add(row);
                }
            }
            commitTransaction();
        } catch (SQLException e) {
            rollbackTransaction();
            throw e;
        }
        return results;
    }

    /**
     * Exécute une requête de mise à jour (INSERT, UPDATE, DELETE).
     * @param query La requête SQL à exécuter.
     * @param parameters Les paramètres de la requête.
     * @return Un HashMap contenant le statut de l'exécution.
     * @throws SQLException En cas d'erreur SQL.
     */
    private HashMap<String, String> executeUpdate(String query, List<Object> parameters) throws SQLException {
        HashMap<String, String> result = new HashMap<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            beginTransaction();
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            if (getAffectedRows(query, pstmt) > 0) {
                result.put(Constants.STATUS, Constants.SUCCESS);
            } else {
                result.put(Constants.STATUS, Constants.FAILURE);
            }
            commitTransaction();
        } catch (SQLException e) {
            rollbackTransaction();
            result.put(Constants.STATUS, Constants.ERROR);
            throw e;
        }
        return result;
    }

    /**
     * Méthode pour obtenir le nombre de lignes affectées par une mise à jour.
     * @param query La requête SQL.
     * @param pstmt Le PreparedStatement à exécuter.
     * @return Le nombre de lignes affectées.
     */
    private static int getAffectedRows(String query, PreparedStatement pstmt) {
        int affectedRows = 0;
        try {
            affectedRows = pstmt.executeUpdate();
        } catch (Exception e) {
            logger.severe("Une erreur est survenue avec la requete " + query);
        }
        return affectedRows;
    }

    /**
     * Méthode pour construire une requête SELECT.
     * @param tableName Le nom de la table.
     * @param columnNames Les colonnes à sélectionner.
     * @param whereClause Les clauses WHERE.
     * @return La requête SQL construite.
     */
    private String buildSelectQuery(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        StringBuilder query = new StringBuilder("SELECT");
        query.append(" ").append(String.join(", ", columnNames));
        query.append(" FROM ").append(tableName);
        if (!whereClause.isEmpty()) {
            query.append(" WHERE ");
            query.append(String.join(Constants.AND_QUERY, whereClause.keySet())).append(Constants.AND_PARAMETER);
        }
        return query.toString();
    }

    /**
     * Méthode pour sélectionner un enregistrement dans la base de données.
     * @param tableName Le nom de la table.
     * @param columnNames Les colonnes à sélectionner.
     * @param whereClause Les clauses WHERE.
     * @return Un HashMap contenant le résultat.
     */
    @Override
    public HashMap<String, String> select(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        String query = buildSelectQuery(tableName, columnNames, whereClause);
        List<Object> parameters = new ArrayList<>(whereClause.values());

        try {
            return executeQuery(query, parameters, columnNames);
        } catch (SQLException e) {
            logger.severe("Erreur de récupération des données: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Méthode pour sélectionner tous les enregistrements correspondant aux critères dans la base de données.
     * @param tableName Le nom de la table.
     * @param columnNames Les colonnes à sélectionner.
     * @param whereClause Les clauses WHERE.
     * @return Une ArrayList contenant les résultats.
     */
    @Override
    public ArrayList<HashMap<String, String>> selectAll(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        String query = buildSelectQuery(tableName, columnNames, whereClause);
        List<Object> parameters = new ArrayList<>(whereClause.values());

        try {
            return executeMultiResultQuery(query, parameters, columnNames);
        } catch (SQLException e) {
            logger.severe("Erreur de récupération des données: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Méthode pour insérer un enregistrement dans la base de données.
     * @param tableName Le nom de la table.
     * @param columnAndValue Les colonnes et leurs valeurs.
     * @return Un HashMap contenant le statut de l'insertion.
     */
    @Override
    public HashMap<String, String> insert(String tableName, LinkedHashMap<String, String> columnAndValue) {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName).append(" (");
        query.append(String.join(", ", columnAndValue.keySet()));
        query.append(") VALUES (");
        query.append("?, ".repeat(columnAndValue.size() - 1)).append("?)");

        List<Object> parameters = List.copyOf(columnAndValue.values());
        try {
            return executeUpdate(query.toString(), parameters);
        } catch (SQLException e) {
            logger.severe("Erreur d'insertion des données: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Méthode pour mettre à jour un enregistrement dans la base de données.
     * @param tableName Le nom de la table.
     * @param columnAndValue Les colonnes et leurs valeurs à mettre à jour.
     * @param whereClause Les clauses WHERE.
     * @return Un HashMap contenant le statut de la mise à jour.
     */
    @Override
    public HashMap<String, String> update(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause) {
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(tableName).append(" SET ");
        query.append(String.join(" = ?, ", columnAndValue.keySet())).append(" = ? WHERE ");
        query.append(String.join(Constants.AND_QUERY, whereClause.keySet())).append(Constants.AND_PARAMETER);

        List<Object> parameters = new ArrayList<>(List.copyOf(columnAndValue.values()));
        parameters.addAll(whereClause.values());

        try {
            return executeUpdate(query.toString(), parameters);
        } catch (SQLException e) {
            logger.severe("Erreur de mise à jour des données: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Méthode pour supprimer un enregistrement dans la base de données.
     * @param tableName Le nom de la table.
     * @param whereClause Les clauses WHERE.
     * @return Un HashMap contenant le statut de la suppression.
     */
    @Override
    public HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause) {
        StringBuilder query = new StringBuilder("DELETE FROM ");
        query.append(tableName).append(" WHERE ");
        query.append(String.join(Constants.AND_QUERY, whereClause.keySet())).append(Constants.AND_PARAMETER);

        List<Object> parameters = List.copyOf(whereClause.values());

        try {
            return executeUpdate(query.toString(), parameters);
        } catch (SQLException e) {
            logger.severe("Erreur de suppression des données: " + e.getMessage());
            return new HashMap<>();
        }
    }
}
