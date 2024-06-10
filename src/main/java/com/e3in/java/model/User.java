package com.e3in.java.model;

public class User {
    private String login;
    private String password;
    private boolean isAdmin;

    public User() {
        this.login = "";
        this.isAdmin = false;
    }

    public User (String email, boolean isAdmin) {
        this.login = email;
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }
}
