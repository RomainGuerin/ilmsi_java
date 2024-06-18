package com.e3in.java.controller;

import com.e3in.java.dao.UserDAO;
import com.e3in.java.model.User;
import com.e3in.java.utils.Common;
import javafx.scene.control.Alert;

public class UserController {
    private final UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // TODO : Dans le controller il manipule la view ??, le controller peux évaluer la cohérence des champs oui, mais pas dans la class user (Doute voir avec prof)
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
