package com.e3in.java.controller;

import com.e3in.java.model.Auteur;
import com.e3in.java.model.Livre;

public class BookController {
    private Livre livre;
    public String getTitre() {
        return this.livre.getTitre();
    }

    public void setTitre(String title) {
        this.livre.setTitre(title);
    }

    public Auteur getAuteur() {
        return this.getAuteur();
    }

    public void setAuteur(Auteur auteur) {
        this.livre.setAuteur(auteur);
    }

    public String getPresentation() {
        return this.livre.getPresentation();
    }

    public void setPresentation(String presentation) {
        this.livre.setPresentation(presentation);
    }

    public int getParution() {
        return this.livre.getParution();
    }

    public void setParution(int parution) {
        this.livre.setParution(parution);
    }

    public int getColonne() {
        return this.livre.getColonne();
    }

    public void setColonne(int colonne) {
        this.livre.setColonne(colonne);
    }

    public int getRangee() {
        return this.livre.getRangee();
    }

    public void setRangee(int rangee) {
        this.livre.setRangee(rangee);
    }
}
