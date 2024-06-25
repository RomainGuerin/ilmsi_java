package com.e3in.java.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Classe Singleton pour gérer la connexion SQLite.
 */
public final class SQLiteConnection {
    // Instance unique de SQLiteConnection
    private static SQLiteConnection instance = null;
    // Objet de connexion à la base de données
    private static Connection connection = null;
    private static final String DB_NAME = "javaLibrary.db";
    private static final String DB_PATH = System.getProperty("user.home") + File.separator + DB_NAME;

    static Logger logger = Logger.getLogger(SQLiteConnection.class.getName());

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

            File dbFile = Common.createOrGetFile(DB_NAME, DB_PATH, SQLiteConnection.class);

            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            logger.info("Connexion à SQLite établie avec succès. Database path: " + dbFile.getAbsolutePath());
        } catch (ClassNotFoundException e) {
            logger.severe("Erreur de chargement du driver JDBC pour SQLite. " + e.getMessage());
        } catch (SQLException e) {
            logger.severe("Erreur de connexion à la base de données SQLite. " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Erreur inattendue. " + e.getMessage());
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
                logger.info("Connexion à SQLite fermée avec succès.");
            }
        } catch (SQLException e) {
            logger.severe("Erreur de fermeture de la connexion à la base de données SQLite. " + e.getMessage());
        }
    }
}
