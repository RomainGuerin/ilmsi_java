package com.e3in.java.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * Représente une bibliothèque de livres.
 */
@XmlRootElement(name = "bibliotheque")
public class Bibliotheque {
    private List<Livre> livres;

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
}
