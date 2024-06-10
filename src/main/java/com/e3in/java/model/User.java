package com.e3in.java.model;

public class User {
    private String login;
    private String password;
    private boolean isAdmin;
    public User (String email, String password) {

    }
    public boolean isAdmin() {
        return this.isAdmin;
    }
}
