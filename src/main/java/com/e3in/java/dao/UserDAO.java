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
    private final DAOManager daoManager;

    public UserDAO(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    public User getUserByEmailPassword(User user) {
        HashMap<String, String> whereClause = new HashMap<>();
        whereClause.put(Constants.EMAIL, user.getEmail());

        List<String> columnNames = List.of(
                Constants.ID,
                Constants.EMAIL,
                Constants.PASSWORD,
                Constants.TYPE
        );

        HashMap<String, String> resultQuery = daoManager.select(Constants.USER, columnNames, whereClause);

        if (!resultQuery.isEmpty()) {
            String hashedPassword = resultQuery.get(Constants.PASSWORD);
            if (BCrypt.checkpw(user.getPassword(), hashedPassword)) {
                boolean isAdmin = Objects.equals(resultQuery.get(Constants.TYPE), Constants.ADMIN);
                return new User(Integer.parseInt(resultQuery.get(Constants.ID)), resultQuery.get(Constants.EMAIL), isAdmin);
            } else {
                Common.showAlert(Alert.AlertType.ERROR, "Identifiants invalides", "Email ou mot de passe incorrect. Veuillez réessayer.");
            }
        } else {
            Common.showAlert(Alert.AlertType.ERROR, "Utilisateur introuvable", "Aucun compte trouvé avec l'email fourni.");
        }
        return null;
    }

    public boolean createUser(User user) {
        LinkedHashMap<String, String> userData = new LinkedHashMap<>();
        userData.put(Constants.EMAIL, user.getEmail());
        userData.put(Constants.PASSWORD, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userData.put(Constants.TYPE, user.isAdmin() ? Constants.ADMIN : Constants.USER);

        HashMap<String, String> resultQuery = daoManager.insert(Constants.USER, userData);

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
        userData.put(Constants.PASSWORD, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        HashMap<String, String> whereClause = new HashMap<>();
        whereClause.put(Constants.EMAIL, user.getEmail());

        HashMap<String, String> resultQuery =  daoManager.update(Constants.USER, userData, whereClause);

        return !resultQuery.isEmpty();
    }
}
