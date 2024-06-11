package com.e3in.java.dao;

import com.e3in.java.utils.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BaseSQL {
    SQLiteConnection connection;

    public BaseSQL() {
        connection = SQLiteConnection.getInstance();
    }

    public HashMap<String, String> get(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        HashMap<String, String> result = new HashMap<>();
        Connection connection = SQLiteConnection.getInstance().getConnection();

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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public HashMap<String, String> set(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause) {
        Connection connection = SQLiteConnection.getInstance().getConnection();
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
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("status", "error");
        }

        return result;
    }
}
