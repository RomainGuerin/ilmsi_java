package com.e3in.java.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une bibliothèque de livres.
 */
@XmlRootElement(name = "bibliotheque")
public class Bibliotheque {
    private List<Livre> livres;

    /**
     * Constructeur par défaut de la bibliothèque.
     */
    public Bibliotheque() {
        livres = new ArrayList<>();
    }

    /**
     * Constructeur de la bibliothèque avec la liste des livres.
     * 
     * @param livres La liste des livres.
     */
    public Bibliotheque(List<Livre> livres) {
        this.livres = livres;
    }

    /**
     * Récupère la liste des livres.
     * 
     * @return La liste des livres.
     */
    @XmlElement(name = "livre")
    public List<Livre> getLivres() {
        return this.livres;
    }

    /**
     * Définit la liste des livres.
     * 
     * @param livres La liste des livres.
     */
    public void setLivres(List<Livre> livres) {
        this.livres = livres;
    }

    public boolean contain(Livre livre) {
        for(Livre actualLivre : this.livres) {
            if(actualLivre.equals(livre)) {
                return true;
            }
        }
        return false;
    }
}
