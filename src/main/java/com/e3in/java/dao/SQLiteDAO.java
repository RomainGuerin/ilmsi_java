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

public class SQLiteDAO extends BaseDAO {

    static Logger logger = Logger.getLogger(SQLiteDAO.class.getName());

    public SQLiteDAO() {
        try {
            this.connection = SQLiteConnection.getInstance().getConnection();
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    private static int getAffectedRows(String query, PreparedStatement pstmt) {
        int affectedRows = 0;
        try {
            affectedRows = pstmt.executeUpdate();
        } catch (Exception e) {
            logger.severe("Une erreur est survenue avec la requete " + query);
        }
        return affectedRows;
    }

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
