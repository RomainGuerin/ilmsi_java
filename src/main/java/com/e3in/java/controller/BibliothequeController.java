package com.e3in.java.controller;

import com.e3in.java.dao.BibliothequeDAO;
import com.e3in.java.model.Bibliotheque;
import com.e3in.java.model.Livre;

/**
 * Contrôleur pour la gestion de la bibliothèque.
 * Ce contrôleur utilise un BibliothequeDAO pour interagir avec la base de données ou le système de stockage des données.
 */
public class BibliothequeController {

    private final BibliothequeDAO bibliothequeDAO;

    /**
     * Constructeur du contrôleur de bibliothèque.
     *
     * @param bibliothequeDAO Instance du DAO de bibliothèque à utiliser.
     */
    public BibliothequeController(BibliothequeDAO bibliothequeDAO) {
        this.bibliothequeDAO = bibliothequeDAO;
    }

    /**
     * Récupère toutes les informations de la bibliothèque.
     *
     * @return La bibliothèque complète.
     */
    public Bibliotheque getAllBibliotheque() {
        return bibliothequeDAO.getAllBibliotheque();
    }

    /**
     * Met à jour la bibliothèque avec les informations fournies.
     * Ajoute chaque livre de la bibliothèque à la base de données.
     *
     * @param bibliotheque La bibliothèque à mettre à jour.
     * @param removeLivreAvailable true si on gère la suppression des livres
     */
    public boolean updateBibliotheque(Bibliotheque bibliotheque, boolean removeLivreAvailable) {
        Bibliotheque library = getAllBibliotheque();

        boolean result;

        if (synchroBibliotheque(bibliotheque, library)) return false;

        if (removeLivreAvailable) {
            for (Livre livre : library.getLivres()) {
                if (!bibliotheque.contain(livre)) {
                    result = this.removeLivreBibliotheque(livre);
                    if (!result) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean synchroBibliotheque(Bibliotheque bibliotheque, Bibliotheque library) {
        boolean result;
        for(Livre livre : bibliotheque.getLivres()) {
            if (!library.contain(livre)) {
                result = this.addLivreBibliotheque(livre);
            } else {
                result = this.updateLivreBibliotheque(livre);
            }
            if (!result) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ajoute un livre à la bibliothèque.
     *
     * @param livre Le livre à ajouter.
     * @return True si l'ajout est réussi, sinon False.
     */
    public boolean addLivreBibliotheque(Livre livre) {
        return bibliothequeDAO.addLivreBibliotheque(livre);
    }

    /**
     * Met à jour les informations d'un livre dans la bibliothèque.
     *
     * @param livre Le livre à mettre à jour.
     * @return True si la mise à jour est réussie, sinon False.
     */
    public boolean updateLivreBibliotheque(Livre livre) {
        return bibliothequeDAO.updateLivreBibliotheque(livre);
    }

    /**
     * Supprime un livre de la bibliothèque.
     *
     * @param livre Le livre à supprimer.
     * @return True si la suppression est réussie, sinon False.
     */
    public boolean removeLivreBibliotheque(Livre livre) {
        return bibliothequeDAO.removeLivreBibliotheque(livre);
    }
}
