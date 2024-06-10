package com.e3in.java.controller;

import com.e3in.java.model.User;
import com.e3in.java.utils.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    SQLiteConnection connection;
    User user;

    public UserController() {
        connection = SQLiteConnection.getInstance();
        user = new User();
    }

    public User signIn(String login, String password) {
        List<Object> elements = new ArrayList<>();
        Connection conn = connection.getConnection();
        try {
            String query = "INSERT INTO user (email, password) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                boolean type = rs.getInt("type") == 1;
                user = new User(rs.getString("email"), type);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur." + e.getMessage());
        } finally {
            SQLiteConnection.close();
        }
        return user;
    }
}
