package com.e3in.java.dao;

import com.e3in.java.utils.SQLiteConnection;

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

    private HashMap<String, String> executeUpdate(String query, List<Object> parameters) throws SQLException {
        HashMap<String, String> result = new HashMap<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            beginTransaction();
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                result.put("status", "success");
            } else {
                result.put("status", "failure");
            }
            commitTransaction();
        } catch (SQLException e) {
            rollbackTransaction();
            result.put("status", "error");
            throw e;
        }
        return result;
    }

    @Override
    public HashMap<String, String> select(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(String.join(", ", columnNames));
        query.append(" FROM ").append(tableName).append(" WHERE ");
        query.append(String.join(" = ? AND ", whereClause.keySet())).append(" = ?");

        List<Object> parameters = List.copyOf(whereClause.values());
        try {
            return executeQuery(query.toString(), parameters, columnNames);
        } catch (SQLException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public HashMap<String, String> insert(String tableName, HashMap<String, String> columnAndValue) {
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
        query.append(String.join(" = ? AND ", whereClause.keySet())).append(" = ?");

        List<Object> parameters = List.copyOf(columnAndValue.values());
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
        query.append(String.join(" = ? AND ", whereClause.keySet())).append(" = ?");

        List<Object> parameters = List.copyOf(whereClause.values());

        try {
            return executeUpdate(query.toString(), parameters);
        } catch (SQLException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
