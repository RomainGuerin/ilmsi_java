package com.e3in.java.controller;

import com.e3in.java.dao.BibliothequeDAO;
import com.e3in.java.model.Bibliotheque;
import com.e3in.java.model.Livre;

public class BibliothequeController {
    private final BibliothequeDAO bibliothequeDAO;

    public BibliothequeController(BibliothequeDAO bibliothequeDAO) {
        this.bibliothequeDAO = bibliothequeDAO;
    }

    public Bibliotheque getAllBibliotheque() {
        return bibliothequeDAO.getAllBibliotheque();
    }

    public void updateBibliotheque(Bibliotheque bibliotheque, boolean isOnline) {
        Bibliotheque library = getAllBibliotheque();

        for(Livre livre : bibliotheque.getLivres()) {
            if (!library.contain(livre)) {
                bibliothequeDAO.addLivreBibliotheque(livre);
            } else {
                bibliothequeDAO.updateLivreBibliotheque(livre);
            }
        }

        if (isOnline) {
            for (Livre livre : library.getLivres()) {
                if (!bibliotheque.contain(livre)) {
                    bibliothequeDAO.removeLivreBibliotheque(livre);
                }
            }
        }
    }

    public boolean addLivreBibliotheque(Livre livre) {
        return bibliothequeDAO.addLivreBibliotheque(livre);
    }

    public boolean updateLivreBibliotheque(Livre livre) {
        return bibliothequeDAO.updateLivreBibliotheque(livre);
    }

    public boolean removeLivreBibliotheque(Livre livre) {
        return bibliothequeDAO.removeLivreBibliotheque(livre);
    }
}
