package com.e3in.java.controller;

import com.e3in.java.dao.UserDAO;
import com.e3in.java.model.User;
import com.e3in.java.utils.Common;
import javafx.scene.control.Alert;

import java.util.HashMap;

public class UserController {
    private final UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public static boolean checkValidity(User user) {
        if (user.isEmailValid()) {
            Common.showAlert(Alert.AlertType.ERROR, "Email invalide", "Insérez une adresse email valide.");
            return false;
        }
        if (user.isPasswordValid()) {
            Common.showAlert(Alert.AlertType.ERROR, "Mot de passe invalide", "Le mot de passe ne peut pas être vide.");
            return false;
        }
        return true;
    }

    public HashMap<String, String> getUserById(String userId) {
        return userDAO.getUserById(userId);
    }

    public HashMap<String, String> getUserByEmailPassword(String email, String password) {
        return userDAO.getUserByEmailPassword(email, password);
    }

    public User getUserByEmail(User user) {
        return userDAO.getUserByEmail(user);
    }

    public boolean createUser(User user) {
        return userDAO.createUser(user);
    }

    public HashMap<String, String> updateUser(String userId, HashMap<String, String> userData) {
        return userDAO.updateUser(userId, userData);
    }

    public HashMap<String, String> deleteUser(String userId) {
        return userDAO.deleteUser(userId);
    }
}
