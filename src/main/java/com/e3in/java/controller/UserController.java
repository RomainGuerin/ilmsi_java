package com.e3in.java.controller;

import com.e3in.java.dao.UserDAO;
import com.e3in.java.model.User;

import java.util.HashMap;

public class UserController {
    private final UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public static HashMap<String, String> checkValidity(User user) {
        HashMap<String, String> errors = new HashMap<>();
        if (user.isEmailValid()) {
            errors.put("error", "Email invalide");
            errors.put("message", "Insérez une adresse email valide.");
        }
        if (user.isPasswordValid()) {
            errors.put("error", "Mot de passe invalide");
            errors.put("message", "Le mot de passe ne peut pas être vide.");
        }
        return errors;
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
