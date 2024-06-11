package com.e3in.java.utils;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class SQLiteConnection {
    private static SQLiteConnection instance = null;
    private static Connection connection = null;

    private SQLiteConnection() {
        connect();
    }

    public static SQLiteConnection getInstance() {
        if (SQLiteConnection.instance == null) {
            synchronized(SQLiteConnection.class) {
                if (SQLiteConnection.instance == null) {
                    SQLiteConnection.instance = new SQLiteConnection();
                }
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
            URL res = SQLiteConnection.class.getClassLoader().getResource("javaLibrary2.db");
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
                System.out.println("Connexion à SQLite fermée avec succès.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur de fermeture de la connexion à la base de données SQLite.");
            e.printStackTrace();
        }
    }
}
