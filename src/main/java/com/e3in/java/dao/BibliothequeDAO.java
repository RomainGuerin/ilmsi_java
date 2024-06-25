package com.e3in.java.dao;

import com.e3in.java.model.Auteur;
import com.e3in.java.model.Bibliotheque;
import com.e3in.java.model.Livre;
import com.e3in.java.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * La classe BibliothequeDAO fournit des méthodes pour gérer les opérations de base de données
 * liées à la bibliothèque, telles que l'ajout, la mise à jour, la suppression et la récupération des livres.
 */
public class BibliothequeDAO {
    private final DAOManager daoManager;

    /**
     * Constructeur pour initialiser le DAOManager.
     * @param daoManager L'instance de DAOManager à utiliser.
     */
    public BibliothequeDAO(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    /**
     * Récupère tous les livres de la bibliothèque depuis la base de données.
     * @return Un objet Bibliotheque contenant tous les livres.
     */
    public Bibliotheque getAllBibliotheque() {
        List<String> columnNames = List.of(
                Constants.TITRE,
                Constants.AUTEUR_NOM,
                Constants.AUTEUR_PRENOM,
                Constants.PRESENTATION,
                Constants.JAQUETTE,
                Constants.PARUTION,
                Constants.COLONNE,
                Constants.RANGEE,
                Constants.EMPRUNTE
        );
        ArrayList<HashMap<String, String>> resultQuery = daoManager.selectAll(Constants.TABLE_LIVRE, columnNames, new HashMap<>());
        return getBibliotheque(resultQuery);
    }

    /**
     * Ajoute un livre à la bibliothèque.
     * @param livre L'objet Livre à ajouter.
     * @return true si l'ajout a réussi, false sinon.
     */
    public boolean addLivreBibliotheque(Livre livre) {
        LinkedHashMap<String, String> map = getMapLivreComplete(livre);

        HashMap<String, String> resultQuery = daoManager.insert(Constants.TABLE_LIVRE, map);
        return !resultQuery.isEmpty() && resultQuery.get(Constants.STATUS).equals(Constants.SUCCESS);
    }

    /**
     * Met à jour un livre dans la bibliothèque.
     * @param livre L'objet Livre à mettre à jour.
     * @return true si la mise à jour a réussi, false sinon.
     */
    public boolean updateLivreBibliotheque(Livre livre) {
        HashMap<String, String> resultQuery = daoManager.update(Constants.TABLE_LIVRE, getMapLivre(livre), getMapLivreUnique(livre));
        return !resultQuery.isEmpty() && resultQuery.get(Constants.STATUS).equals(Constants.SUCCESS);
    }

    /**
     * Supprime un livre de la bibliothèque.
     * @param livre L'objet Livre à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    public boolean removeLivreBibliotheque(Livre livre) {
        HashMap<String, String> resultQuery = daoManager.delete(Constants.TABLE_LIVRE, getMapLivreUnique(livre));
        return !resultQuery.isEmpty() && resultQuery.get(Constants.STATUS).equals(Constants.SUCCESS);
    }

    /**
     * Convertit le résultat de la requête SQL en objet Bibliotheque.
     * @param resultQuery Le résultat de la requête SQL.
     * @return Un objet Bibliotheque contenant les livres.
     */
    private static Bibliotheque getBibliotheque(ArrayList<HashMap<String, String>> resultQuery) {
        Bibliotheque bibliotheque = new Bibliotheque();
        for (HashMap<String, String> map : resultQuery) {
            Livre livre = new Livre();
            livre.setTitre(map.get(Constants.TITRE));
            livre.setAuteur(new Auteur(map.get(Constants.AUTEUR_NOM), map.get(Constants.AUTEUR_PRENOM)));
            livre.setPresentation(map.get(Constants.PRESENTATION));
            livre.setJaquette(map.get(Constants.JAQUETTE));
            livre.setParution(Integer.parseInt(map.get(Constants.PARUTION)));
            livre.setColonne(Integer.parseInt(map.get(Constants.COLONNE)));
            livre.setRangee(Integer.parseInt(map.get(Constants.RANGEE)));
            livre.setEmprunte(parseEmprunte(map.get(Constants.EMPRUNTE)));
            bibliotheque.getLivres().add(livre);
        }
        return bibliotheque;
    }

    private static boolean parseEmprunte(String value) {
        return "1".equals(value) || "true".equalsIgnoreCase(value);
    }

    /**
     * Convertit un objet Livre en map complète pour l'insertion dans la base de données.
     * @param livre L'objet Livre à convertir.
     * @return Une LinkedHashMap contenant les colonnes et leurs valeurs.
     */
    private LinkedHashMap<String, String> getMapLivreComplete (Livre livre) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(Constants.TITRE, livre.getTitre());
        map.put(Constants.AUTEUR_NOM, livre.getAuteur().getNom());
        map.put(Constants.AUTEUR_PRENOM, livre.getAuteur().getPrenom());
        map.put(Constants.PRESENTATION, livre.getPresentation());
        map.put(Constants.JAQUETTE, livre.getJaquette());
        map.put(Constants.PARUTION, String.valueOf(livre.getParution()));
        map.put(Constants.COLONNE, String.valueOf(livre.getColonne()));
        map.put(Constants.RANGEE, String.valueOf(livre.getRangee()));
        map.put(Constants.EMPRUNTE, livre.getEmprunte() ? "1" : "0");
        return map;
    }

    /**
     * Convertit un objet Livre en map pour la mise à jour dans la base de données.
     * @param livre L'objet Livre à convertir.
     * @return Une HashMap contenant les colonnes et leurs valeurs à mettre à jour.
     */
    private HashMap<String, String> getMapLivre(Livre livre) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.PRESENTATION, livre.getPresentation());
        map.put(Constants.JAQUETTE, livre.getJaquette());
        map.put(Constants.COLONNE, String.valueOf(livre.getColonne()));
        map.put(Constants.RANGEE, String.valueOf(livre.getRangee()));
        map.put(Constants.EMPRUNTE, livre.getEmprunte() ? "1" : "0");
        return map;
    }

    /**
     * Convertit un objet Livre en map unique pour l'identification dans la base de données.
     * @param livre L'objet Livre à convertir.
     * @return Une HashMap contenant les colonnes et leurs valeurs pour l'identification unique.
     */
    private HashMap<String, String> getMapLivreUnique(Livre livre) {
        HashMap<String, String> mapLivreUnique = new HashMap<>();
        mapLivreUnique.put(Constants.TITRE, livre.getTitre());
        mapLivreUnique.put(Constants.AUTEUR_NOM, livre.getAuteur().getNom());
        mapLivreUnique.put(Constants.AUTEUR_PRENOM, livre.getAuteur().getPrenom());
        mapLivreUnique.put(Constants.PARUTION, String.valueOf(livre.getParution()));
        return mapLivreUnique;
    }
}
