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

    public boolean updateBibliotheque(Bibliotheque bibliotheque, boolean removeLivreAvailable) {
        Bibliotheque library = getAllBibliotheque();

        boolean result;

        for(Livre livre : bibliotheque.getLivres()) {
            if (!library.contain(livre)) {
                result = bibliothequeDAO.addLivreBibliotheque(livre);
            } else {
                result = bibliothequeDAO.updateLivreBibliotheque(livre);
            }
            if (!result) {
                return false;
            }
        }

        if (removeLivreAvailable) {
            for (Livre livre : library.getLivres()) {
                if (!bibliotheque.contain(livre)) {
                    result = bibliothequeDAO.removeLivreBibliotheque(livre);
                    if (!result) {
                        return false;
                    }
                }
            }
        }
        return true;
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
