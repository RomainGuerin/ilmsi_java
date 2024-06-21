package com.e3in.java.utils;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe Singleton pour gérer la connexion SQLite.
 */
public final class SQLiteConnection {
    // Instance unique de SQLiteConnection
    private static SQLiteConnection instance = null;
    // Objet de connexion à la base de données
    private static Connection connection = null;

    // Constructeur privé pour empêcher l'instanciation directe
    private SQLiteConnection() {
        connect();
    }

    /**
     * Retourne l'instance unique de SQLiteConnection.
     * @return l'instance unique de SQLiteConnection.
     */
    public static SQLiteConnection getInstance() {
        if (SQLiteConnection.instance == null) {
            // Bloc synchronized pour assurer que l'instance est unique même en cas d'accès concurrent
            synchronized(SQLiteConnection.class) {
                SQLiteConnection.instance = new SQLiteConnection();
            }
        }
        return SQLiteConnection.instance;
    }

    /**
     * Établit la connexion à la base de données SQLite.
     */
    private static void connect() {
        try {
            // Chargement du driver JDBC pour SQLite
            Class.forName("org.sqlite.JDBC");

            // Chemin de la base de données
            // TODO passer en argument le nom de la DB
            URL res = SQLiteConnection.class.getClassLoader().getResource("javaLibrary.db");
            System.out.println(res);
            if (res == null) {
                throw new SQLException("Impossible de trouver le fichier de base de données");
            }
            String dbPath = Paths.get(res.toURI()).toString();

            // Connexion à la base de données
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            System.out.println("Connexion à SQLite établie avec succès.");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur de chargement du driver JDBC pour SQLite.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données SQLite.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur inattendue.");
            e.printStackTrace();
        }
    }

    /**
     * Retourne l'objet de connexion à la base de données.
     * @return l'objet de connexion.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Ferme la connexion à la base de données lorsqu'elle est finalisée.
     */
    @SuppressWarnings("removal")
    protected void finalize() {
        close();
    }

    /**
     * Ferme explicitement la connexion à la base de données.
     */
    public static void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connexion à SQLite fermée avec succès.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur de fermeture de la connexion à la base de données SQLite.");
            e.printStackTrace();
        }
    }
}
