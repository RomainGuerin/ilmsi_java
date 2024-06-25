package com.e3in.java.model;

public class User {
    private int id;
    private String email;
    private String password;
    private boolean isAdmin;

    /**
     * Constructeur par défaut
     */
    public User() {}

    /**
     * Constructeur avec email et mot de passe
     * @param email L'email de l'utilisateur
     * @param password Le mot de passe de l'utilisateur
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.isAdmin = false; // Par défaut, l'utilisateur n'est pas administrateur
    }

    /**
     * Constructeur avec id, email et mot de passe
     * @param id L'identifiant de l'utilisateur
     * @param email L'email de l'utilisateur
     * @param password Le mot de passe de l'utilisateur
     */
    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isAdmin = false; // Par défaut, l'utilisateur n'est pas administrateur
    }

    /**
     * Constructeur avec id, email, mot de passe et statut d'administrateur
     * @param id L'identifiant de l'utilisateur
     * @param email L'email de l'utilisateur
     * @param password Le mot de passe de l'utilisateur
     * @param isAdmin Le statut d'administrateur de l'utilisateur
     */
    public User(int id, String email, String password, boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    /**
     * Constructeur avec id, email et statut d'administrateur
     * @param id L'identifiant de l'utilisateur
     * @param email L'email de l'utilisateur
     * @param isAdmin Le statut d'administrateur de l'utilisateur
     */
    public User(int id, String email, boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    /**
     * Constructeur avec email, mot de passe et statut d'administrateur
     * @param email L'email de l'utilisateur
     * @param password Le mot de passe de l'utilisateur
     * @param isAdmin Le statut d'administrateur de l'utilisateur
     */
    public User(String email, String password, boolean isAdmin) {
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    /**
     * Obtient l'identifiant de l'utilisateur.
     * @return L'identifiant de l'utilisateur.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Définit l'identifiant de l'utilisateur.
     * @param id L'identifiant de l'utilisateur.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient l'email de l'utilisateur.
     * @return L'email de l'utilisateur.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Définit l'email de l'utilisateur.
     * @param email L'email de l'utilisateur.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Vérifie la validité de l'email de l'utilisateur.
     * @return true si l'email est invalide, false sinon.
     */
    public boolean isEmailValid() {
        return this.email == null || this.email.isEmpty() || !this.email.contains("@");
    }

    /**
     * Obtient le mot de passe de l'utilisateur.
     * @return Le mot de passe de l'utilisateur.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Vérifie la validité du mot de passe de l'utilisateur.
     * @return true si le mot de passe est invalide, false sinon.
     */
    public boolean isPasswordValid() {
        return this.password == null || this.password.isEmpty();
    }

    /**
     * Définit le statut d'administrateur de l'utilisateur.
     * @param isAdmin Le statut d'administrateur de l'utilisateur.
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * Obtient le statut d'administrateur de l'utilisateur.
     * @return true si l'utilisateur est administrateur, false sinon.
     */
    public boolean isAdmin() {
        return this.isAdmin;
    }
}
