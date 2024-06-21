package com.e3in.java.utils;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public final class SQLiteConnection {
    private static SQLiteConnection instance = null;
    private static Connection connection = null;

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
            logger.info("Connexion à SQLite établie avec succès.");
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
