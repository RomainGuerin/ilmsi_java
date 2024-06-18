package com.e3in.java.dao;

import com.e3in.java.model.User;
import com.e3in.java.utils.Common;
import javafx.scene.control.Alert;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class UserDAO {
    private final DAOManager daoManager;

    public UserDAO(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    public HashMap<String, String> getUserById(String userId) {
        HashMap<String, String> whereClause = new HashMap<>();
        whereClause.put("id", userId);
        return daoManager.select("user", List.of("id", "email", "password","type"), whereClause);
    }

    public User getUserByEmailPassword(User user) {
        HashMap<String, String> whereClause = new HashMap<>();
        whereClause.put("email", user.getEmail());

        HashMap<String, String> resultQuery = daoManager.select("user", List.of("id", "email", "password", "type"), whereClause);

        if (!resultQuery.isEmpty()) {
            String hashedPassword = resultQuery.get("password");
            if (BCrypt.checkpw(user.getPassword(), hashedPassword)) {
                boolean isAdmin = Objects.equals(resultQuery.get("type"), "admin");
                return new User(Integer.parseInt(resultQuery.get("id")), resultQuery.get("email"), isAdmin);
            } else {
                Common.showAlert(Alert.AlertType.ERROR, "Identifiants invalides", "Email ou mot de passe incorrect. Veuillez réessayer.");
            }
        } else {
            Common.showAlert(Alert.AlertType.ERROR, "Utilisateur introuvable", "Aucun compte trouvé avec l'email fourni.");
        }
        return null;
    }

    public boolean createUser(User user) {
        HashMap<String, String> userData = new HashMap<>();
        userData.put("email", user.getEmail());
        userData.put("password", BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userData.put("type", "user");

        HashMap<String, String> resultQuery = daoManager.insert("user", userData);

        if (!resultQuery.isEmpty()) {
            Common.showAlert(Alert.AlertType.INFORMATION, "Inscription réussie", "Utilisateur enregistré.");
            return true;
        } else {
            Common.showAlert(Alert.AlertType.ERROR, "Erreur lors de l'inscription", "Impossible de réaliser l'inscription, vérifier les informations");
        }
        return false;
    }

    public boolean updatePassword(User user) {
        HashMap<String, String> userData = new HashMap<>();
        userData.put("password", BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        HashMap<String, String> whereClause = new HashMap<>();
        whereClause.put("email", user.getEmail());

        HashMap<String, String> resultQuery =  daoManager.update("user", userData, whereClause);

        return !resultQuery.isEmpty();
    }
}
