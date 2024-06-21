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

public class SQLiteDAO extends BaseDAO {

    // Constructeur de SQLiteDAO
    public SQLiteDAO() {
        try {
            // Obtenez la connexion SQLite et désactivez l'auto-commit
            this.connection = SQLiteConnection.getInstance().getConnection();
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour exécuter une requête SQL et retourner un seul résultat
    private HashMap<String, String> executeQuery(String query, List<Object> parameters, List<String> columnNames) throws SQLException {
        HashMap<String, String> result = new HashMap<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            beginTransaction(); // Commence la transaction
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
            commitTransaction(); // Commit la transaction
        } catch (SQLException e) {
            rollbackTransaction(); // Rollback en cas d'erreur
            throw e;
        }
        return result;
    }

    // Méthode pour exécuter une requête SQL et retourner plusieurs résultats
    private ArrayList<HashMap<String, String>> executeMultiResultQuery(String query, List<Object> parameters, List<String> columnNames) throws SQLException {
        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            beginTransaction(); // Commence la transaction
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
            commitTransaction(); // Commit la transaction
        } catch (SQLException e) {
            rollbackTransaction(); // Rollback en cas d'erreur
            throw e;
        }
        return results;
    }

    // Méthode pour exécuter une requête de mise à jour (INSERT, UPDATE, DELETE)
    private HashMap<String, String> executeUpdate(String query, List<Object> parameters) throws SQLException {
        HashMap<String, String> result = new HashMap<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            beginTransaction(); // Commence la transaction
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            if (getAffectedRows(query, pstmt) > 0) {
                result.put(Constants.STATUS, Constants.SUCCESS);
            } else {
                result.put(Constants.STATUS, Constants.FAILURE);
            }
            commitTransaction(); // Commit la transaction
        } catch (SQLException e) {
            rollbackTransaction(); // Rollback en cas d'erreur
            result.put(Constants.STATUS, Constants.ERROR);
            throw e;
        }
        return result;
    }

    // Méthode pour obtenir le nombre de lignes affectées par une mise à jour
    private static int getAffectedRows(String query, PreparedStatement pstmt) {
        int affectedRows = 0;
        try {
            affectedRows = pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Une erreur est survenue avec la requête " + query);
        }
        return affectedRows;
    }

    // Méthode pour construire une requête SELECT
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

    // Méthode pour sélectionner un enregistrement dans la base de données
    @Override
    public HashMap<String, String> select(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        String query = buildSelectQuery(tableName, columnNames, whereClause);
        List<Object> parameters = new ArrayList<>(whereClause.values());

        try {
            return executeQuery(query, parameters, columnNames);
        } catch (SQLException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // Méthode pour sélectionner tous les enregistrements correspondant aux critères dans la base de données
    @Override
    public ArrayList<HashMap<String, String>> selectAll(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        String query = buildSelectQuery(tableName, columnNames, whereClause);
        List<Object> parameters = new ArrayList<>(whereClause.values());

        try {
            return executeMultiResultQuery(query, parameters, columnNames);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Méthode pour insérer un enregistrement dans la base de données
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
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // Méthode pour mettre à jour un enregistrement dans la base de données
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
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // Méthode pour supprimer un enregistrement dans la base de données
    @Override
    public HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause) {
        StringBuilder query = new StringBuilder("DELETE FROM ");
        query.append(tableName).append(" WHERE ");
        query.append(String.join(Constants.AND_QUERY, whereClause.keySet())).append(Constants.AND_PARAMETER);

        List<Object> parameters = List.copyOf(whereClause.values());

        try {
            return executeUpdate(query.toString(), parameters);
        } catch (SQLException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
