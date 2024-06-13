package com.e3in.java.model;

public class User {
    private int id;
    private String email;
    private String password;
    private boolean isAdmin;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.isAdmin = false;
    }

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isAdmin = false;
    }

    public User(int id, String email, String password, boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public User(int id, String email, boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public User(String email, String password, boolean isAdmin) {
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isEmailValid() {
        return this.email == null || this.email.isEmpty() || !this.email.contains("@");
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isPasswordValid() {
        return this.password == null || this.password.isEmpty();
    }
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    public boolean isAdmin() {
        return this.isAdmin;
    }
}
