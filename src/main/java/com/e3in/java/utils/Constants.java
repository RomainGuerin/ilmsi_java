package com.e3in.java.utils;

/**
 * Classe contenant des constantes utilisées dans l'application.
 */
public class Constants {

    // Constructeur privé pour empêcher l'instanciation de la classe
    private Constants() {}

    // Constantes pour les colonnes de la table "livre"
    public static final String TABLE_LIVRE = "livre";
    public static final String TITRE = "titre";
    public static final String AUTEUR_NOM = "auteurNom";
    public static final String AUTEUR_PRENOM = "auteurPrenom";
    public static final String PRESENTATION = "presentation";
    public static final String JAQUETTE = "jaquette";
    public static final String PARUTION = "parution";
    public static final String COLONNE = "colonne";
    public static final String RANGEE = "rangee";
    public static final String EMPRUNTE = "emprunte";

    // Constantes pour les statuts
    public static final String STATUS = "status";
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String ERROR = "error";

    // Constantes pour les requêtes SQL
    public static final String AND_QUERY = " = ? AND ";
    public static final String AND_PARAMETER = " = ?";

    // Constantes pour les colonnes de la table "user"
    public static final String USER = "user";
    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String TYPE = "type";
    public static final String ADMIN = "admin";
}
