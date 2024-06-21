package com.e3in.java.dao;

import com.e3in.java.model.User;
import com.e3in.java.utils.Common;
import com.e3in.java.utils.Constants;
import javafx.scene.control.Alert;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class UserDAO {
    // DAOManager pour la gestion des interactions avec la base de données
    private final DAOManager daoManager;

    // Constructeur pour initialiser le DAOManager
    public UserDAO(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    // Méthode pour obtenir un utilisateur par email et mot de passe
    public User getUserByEmailPassword(User user) {
        // Création d'une clause WHERE pour la requête SQL
        HashMap<String, String> whereClause = new HashMap<>();
        whereClause.put(Constants.EMAIL, user.getEmail());

        // Liste des colonnes à sélectionner
        List<String> columnNames = List.of(
                Constants.ID,
                Constants.EMAIL,
                Constants.PASSWORD,
                Constants.TYPE
        );

        // Exécution de la requête SELECT
        HashMap<String, String> resultQuery = daoManager.select(Constants.USER, columnNames, whereClause);

        // Vérification si l'utilisateur existe
        if (!resultQuery.isEmpty()) {
            String hashedPassword = resultQuery.get(Constants.PASSWORD);
            // Vérification du mot de passe haché
            if (BCrypt.checkpw(user.getPassword(), hashedPassword)) {
                boolean isAdmin = Objects.equals(resultQuery.get(Constants.TYPE), Constants.ADMIN);
                // Création d'un nouvel objet User avec les informations récupérées
                return new User(Integer.parseInt(resultQuery.get(Constants.ID)), resultQuery.get(Constants.EMAIL), isAdmin);
            } else {
                // Affichage d'une alerte si le mot de passe est incorrect
                Common.showAlert(Alert.AlertType.ERROR, "Identifiants invalides", "Email ou mot de passe incorrect. Veuillez réessayer.");
            }
        } else {
            // Affichage d'une alerte si l'utilisateur n'est pas trouvé
            Common.showAlert(Alert.AlertType.ERROR, "Utilisateur introuvable", "Aucun compte trouvé avec l'email fourni.");
        }
        return null;
    }

    // Méthode pour créer un nouvel utilisateur
    public boolean createUser(User user) {
        // Préparation des données utilisateur pour l'insertion
        LinkedHashMap<String, String> userData = new LinkedHashMap<>();
        userData.put(Constants.EMAIL, user.getEmail());
        userData.put(Constants.PASSWORD, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userData.put(Constants.TYPE, user.isAdmin() ? Constants.ADMIN : Constants.USER);

        // Exécution de la requête INSERT
        HashMap<String, String> resultQuery = daoManager.insert(Constants.USER, userData);

        // Vérification du résultat de l'insertion
        if (!resultQuery.isEmpty()) {
            // Affichage d'une alerte si l'inscription réussit
            Common.showAlert(Alert.AlertType.INFORMATION, "Inscription réussie", "Utilisateur enregistré.");
            return true;
        } else {
            // Affichage d'une alerte en cas d'erreur d'inscription
            Common.showAlert(Alert.AlertType.ERROR, "Erreur lors de l'inscription", "Impossible de réaliser l'inscription, vérifier les informations");
        }
        return false;
    }

    // Méthode pour mettre à jour le mot de passe de l'utilisateur
    public boolean updatePassword(User user) {
        // Préparation des données pour la mise à jour
        HashMap<String, String> userData = new HashMap<>();
        userData.put(Constants.PASSWORD, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        // Préparation de la clause WHERE
        HashMap<String, String> whereClause = new HashMap<>();
        whereClause.put(Constants.EMAIL, user.getEmail());

        // Exécution de la requête UPDATE
        HashMap<String, String> resultQuery =  daoManager.update(Constants.USER, userData, whereClause);

        // Retourne vrai si la mise à jour a réussi, faux sinon
        return !resultQuery.isEmpty();
    }
}
