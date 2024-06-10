package com.e3in.java.controller;

import com.e3in.java.model.User;
import com.e3in.java.utils.SQLiteConnection;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserController {
    SQLiteConnection connection;
    User user;

    public UserController() {
        connection = SQLiteConnection.getInstance();
        user = new User();
    }

    // Méthode pour inscrire un nouvel utilisateur
    public boolean signUp(String email, String password) {
        Connection conn = connection.getConnection();
        try {
            // Vérifier si l'email existe déjà
            String checkQuery = "SELECT COUNT(*) FROM user WHERE email = ?";
            PreparedStatement checkPstmt = conn.prepareStatement(checkQuery);
            checkPstmt.setString(1, email);
            ResultSet rs = checkPstmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                System.err.println("L'email est déjà utilisé.");
                return false; // Email déjà utilisé
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            String query = "INSERT INTO user (email, password, type) VALUES (?, ?, 'user')";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, hashedPassword);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Inscription réussie pour l'email : " + email);
                return true; // Inscription réussie
            } else {
                System.err.println("Aucune ligne affectée lors de l'insertion.");
            }


        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
        return false; // Inscription échouée
    }

    // Méthode pour connecter un utilisateur existant
    public User signIn(String email, String password) {
        Connection conn = connection.getConnection();
        try {
            String query = "SELECT * FROM user WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (BCrypt.checkpw(password, storedPassword)) {
                    user.setEmail(rs.getString("email"));
                    user.setPassword(storedPassword);
                    System.out.println("Connexion réussie pour l'email : " + email);
                    return user; // Connexion réussie
                } else {
                    System.err.println("Mot de passe incorrect pour l'email : " + email);
                }
            } else {
                System.err.println("L'utilisateur avec l'email " + email + " n'existe pas.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion de l'utilisateur : " + e.getMessage());
        }
        return null; // Connexion échouée
    }
}
