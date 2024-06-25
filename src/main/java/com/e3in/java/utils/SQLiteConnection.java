package com.e3in.java.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public final class SQLiteConnection {
    private static SQLiteConnection instance = null;
    private static Connection connection = null;
    private static final String DB_NAME = "javaLibrary.db";
    private static final String DB_PATH = System.getProperty("user.home") + File.separator + DB_NAME;

    static Logger logger = Logger.getLogger(SQLiteConnection.class.getName());

    private SQLiteConnection() {
        connect();
    }

    public static SQLiteConnection getInstance() {
        if (SQLiteConnection.instance == null) {
            synchronized(SQLiteConnection.class) {
                SQLiteConnection.instance = new SQLiteConnection();
            }
        }
        return SQLiteConnection.instance;
    }

    private static void connect() {
        try {
            // Driver JDBC pour SQLite
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

    public Connection getConnection() {
        return connection;
    }

    @SuppressWarnings("removal")
    protected void finalize() {
        close();
    }

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
