package com.e3in.java.controller;

import com.e3in.java.dao.UserDAO;
import com.e3in.java.model.User;
import com.e3in.java.utils.Common;
import javafx.scene.control.Alert;

/**
 * Contrôleur pour la gestion des utilisateurs.
 * Gère les opérations liées à la gestion des utilisateurs, telles que la validation, la création et la mise à jour.
 */
public class UserController {
    private final UserDAO userDAO;

    /**
     * Constructeur du contrôleur UserController.
     *
     * @param userDAO Objet UserDAO utilisé pour accéder aux données des utilisateurs.
     */
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Vérifie la validité des informations d'utilisateur.
     *
     * @param user L'utilisateur dont les informations doivent être validées.
     * @return True si les informations sont valides, sinon False.
     */
    public static boolean checkValidity(User user) {
        // Vérifie si l'email est valide
        if (!user.isEmailValid()) {
            Common.showAlert(Alert.AlertType.ERROR, "Email invalide", "Insérez une adresse email valide.");
            return false;
        }
        // Vérifie si le mot de passe est valide
        if (!user.isPasswordValid()) {
            Common.showAlert(Alert.AlertType.ERROR, "Mot de passe invalide", "Le mot de passe ne peut pas être vide.");
            return false;
        }
        return true;
    }

    /**
     * Récupère un utilisateur par email et mot de passe.
     *
     * @param user L'utilisateur contenant l'email et le mot de passe à rechercher.
     * @return L'utilisateur correspondant trouvé dans la base de données, ou null s'il n'existe pas.
     */
    public User getUserByEmailPassword(User user) {
        return userDAO.getUserByEmailPassword(user);
    }

    /**
     * Crée un nouvel utilisateur dans la base de données.
     *
     * @param user L'utilisateur à créer.
     * @return True si l'utilisateur a été créé avec succès, sinon False.
     */
    public boolean createUser(User user) {
        return userDAO.createUser(user);
    }

    /**
     * Met à jour le mot de passe de l'utilisateur dans la base de données.
     *
     * @param user L'utilisateur avec le nouveau mot de passe.
     * @return True si le mot de passe a été mis à jour avec succès, sinon False.
     */
    public boolean updatePassword(User user) {
        return userDAO.updatePassword(user);
    }
}
