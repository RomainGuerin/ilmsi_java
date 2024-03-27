package com.e3in.java.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Contrôleur de la vue À propos
 */
public class AboutViewController {
    @FXML
    private Label versionLabel;

    @FXML
    private Label commitLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label javaVersionLabel;

    @FXML
    private Label osVersionLabel;

    /**
     * Constructeur par défaut de la classe AboutViewController.
     */
    public AboutViewController() { }
    
    /**
     * Initialise la vue À propos.
     * Récupère les informations sur la version de l'application, la date de version,
     * la version de Java et la version de l'OS, puis les affiche dans la vue.
     */
    public void initialize() {
        // Récupérer la version
        String gitVersion = "0.5";

        // Récupérer la date de version
        String versionDate =  "25/03/2024";

        // Version de Java
        String javaVersion = System.getProperty("java.version");

        // Version de l'OS
        String osVersion = System.getProperty("os.version") + " (" + System.getProperty("os.name") + ")";

        // Mise à jour des étiquettes dans le FXML
        versionLabel.setText("Version: " + gitVersion);
        dateLabel.setText("Date de version: " + versionDate);
        javaVersionLabel.setText("Version de Java: " + javaVersion);
        osVersionLabel.setText("Version de l'OS: " + osVersion);
    }
    
    /**
     * Exécute une commande système et renvoie le résultat.
     *
     * @param command La commande à exécuter.
     * @return Le résultat de l'exécution de la commande.
     */
    private String executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return "N/A";
        }
    }
}
