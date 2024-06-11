package com.e3in.java.dao;

import com.e3in.java.utils.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class SQLiteDAO extends BaseDAO {

    public SQLiteDAO() {
        try {
            this.connection = SQLiteConnection.getInstance().getConnection();
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // TODO ajouter une fonction ExecuteRequest pour simplifier le code et eviter les doublons
    @Override
    public HashMap<String, String> get(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        HashMap<String, String> result = new HashMap<>();
        StringBuilder query = new StringBuilder("SELECT ");
        for (int i = 0; i < columnNames.size(); i++) {
            query.append(columnNames.get(i));
            if (i < columnNames.size() - 1) {
                query.append(", ");
            }
        }
        query.append(" FROM ").append(tableName).append(" WHERE ");
        int whereClauseSize = whereClause.size();
        int index = 0;
        for (String key : whereClause.keySet()) {
            query.append(key).append(" = ?");
            if (index < whereClauseSize - 1) {
                query.append(" AND ");
            }
            index++;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query.toString())) {
            beginTransaction();
            index = 1;
            for (String key : whereClause.keySet()) {
                pstmt.setString(index, whereClause.get(key));
                index++;
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
            e.printStackTrace();
            rollbackTransaction();
        }

        return result;
    }

    @Override
    public HashMap<String, String> insert(String tableName, HashMap<String, String> columnAndValue) {
        HashMap<String, String> result = new HashMap<>();
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName).append(" (");

        int setSize = columnAndValue.size();
        int index = 0;
        for (String key : columnAndValue.keySet()) {
            query.append(key);
            if (index < setSize - 1) {
                query.append(", ");
            }
            index++;
        }

        query.append(") VALUES (");
        for (int i = 0; i < setSize; i++) {
            query.append("?");
            if (i < setSize - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        try (PreparedStatement pstmt = connection.prepareStatement(query.toString())) {
            beginTransaction();
            index = 1;
            for (String key : columnAndValue.keySet()) {
                pstmt.setString(index, columnAndValue.get(key));
                index++;
            }

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                result.put("status", "success");
            } else {
                result.put("status", "failure");
            }
            commitTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
            rollbackTransaction();
            result.put("status", "error");
        }

        return result;
    }

    @Override
    public HashMap<String, String> update(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause) {
        HashMap<String, String> result = new HashMap<>();
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(tableName).append(" SET ");

        int setSize = columnAndValue.size();
        int index = 0;
        for (String key : columnAndValue.keySet()) {
            query.append(key).append(" = ?");
            if (index < setSize - 1) {
                query.append(", ");
            }
            index++;
        }

        query.append(" WHERE ");
        int whereClauseSize = whereClause.size();
        index = 0;
        for (String key : whereClause.keySet()) {
            query.append(key).append(" = ?");
            if (index < whereClauseSize - 1) {
                query.append(" AND ");
            }
            index++;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query.toString())) {
            beginTransaction();
            index = 1;
            for (String key : columnAndValue.keySet()) {
                pstmt.setString(index, columnAndValue.get(key));
                index++;
            }
            for (String key : whereClause.keySet()) {
                pstmt.setString(index, whereClause.get(key));
                index++;
            }

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                result.put("status", "success");
            } else {
                result.put("status", "failure");
            }
            commitTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
            rollbackTransaction();
            result.put("status", "error");
        }

        return result;
    }

    @Override
    public HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause) {
        HashMap<String, String> result = new HashMap<>();
        StringBuilder query = new StringBuilder("DELETE FROM ");
        query.append(tableName).append(" WHERE ");

        int whereClauseSize = whereClause.size();
        int index = 0;
        for (String key : whereClause.keySet()) {
            query.append(key).append(" = ?");
            if (index < whereClauseSize - 1) {
                query.append(" AND ");
            }
            index++;
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query.toString())) {
            beginTransaction();
            index = 1;
            for (String key : whereClause.keySet()) {
                pstmt.setString(index, whereClause.get(key));
                index++;
            }

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                result.put("status", "success");
            } else {
                result.put("status", "failure");
            }
            commitTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
            rollbackTransaction();
            result.put("status", "error");
        }

        return result;
    }
}
