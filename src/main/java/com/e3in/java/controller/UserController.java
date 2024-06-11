package com.e3in.java.controller;

import com.e3in.java.dao.UserDAO;

import java.util.HashMap;

public class UserController {
    private final UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public HashMap<String, String> getUserById(String userId) {
        return userDAO.getUserById(userId);
    }

    public HashMap<String, String> getUserByEmailPassword(String email, String password) {
        return userDAO.getUserByEmailPassword(email, password);
    }

    public HashMap<String, String> createUser(HashMap<String, String> userData) {
        return userDAO.createUser(userData);
    }

    public HashMap<String, String> updateUser(String userId, HashMap<String, String> userData) {
        return userDAO.updateUser(userId, userData);
    }

    public HashMap<String, String> deleteUser(String userId) {
        return userDAO.deleteUser(userId);
    }
}
