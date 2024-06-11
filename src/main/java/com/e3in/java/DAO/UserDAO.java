package com.e3in.java.dao;

import java.util.HashMap;
import java.util.List;

public class UserDAO {
    private final DAOManager daoManager;

    public UserDAO(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    public HashMap<String, String> getUserById(String userId) {
        HashMap<String, String> whereClause = new HashMap<>();
        whereClause.put("id", userId);
        return daoManager.get("user", List.of("id", "email", "password","type"), whereClause);
    }

    public HashMap<String, String> getUserByEmailPassword(String email, String password) {
        HashMap<String, String> whereClause = new HashMap<>();
        whereClause.put("email", email);
        whereClause.put("password", password);
        return daoManager.get("user", List.of("id", "email", "password", "type"), whereClause);
    }

    public HashMap<String, String> createUser(HashMap<String, String> userData) {
        return daoManager.insert("user", userData);
    }

    public HashMap<String, String> updateUser(String userId, HashMap<String, String> userData) {
        HashMap<String, String> whereClause = new HashMap<>();
        whereClause.put("id", userId);
        return daoManager.update("user", userData, whereClause);
    }

    public HashMap<String, String> deleteUser(String userId) {
        HashMap<String, String> whereClause = new HashMap<>();
        whereClause.put("id", userId);
        return daoManager.delete("user", whereClause);
    }
}
