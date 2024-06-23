package com.e3in.java.controller;

import com.e3in.java.dao.UserDAO;
import com.e3in.java.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserController {
    private final UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public static Map<String, String> checkValidity(User user) {
        Map<String, String> errorMessage = new HashMap<>();
        if (user.isEmailValid()) {
            errorMessage.put("error", "Email invalide");
            errorMessage.put("message", "Insérez une adresse email valide.");
            return errorMessage;
        }
        if (user.isPasswordValid()) {
            errorMessage.put("error", "Mot de passe invalide");
            errorMessage.put("message", "Le mot de passe ne peut pas être vide.");
            return errorMessage;
        }
        return errorMessage;
    }

    public User getUserByEmailPassword(User user) {
        return userDAO.getUserByEmailPassword(user);
    }

    public boolean createUser(User user) {
        return userDAO.createUser(user);
    }

    public boolean updatePassword(User user) {
        return userDAO.updatePassword(user);
    }
}
