package com.e3in.java.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "livre")
@XmlType(propOrder = {"titre", "auteur", "presentation", "parution", "colonne", "rangee"})
public class Livre {
    private String titre;
    private Auteur auteur;
    private String presentation;
    private int parution;
    private int colonne;
    private int rangee;

    @XmlElement
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @XmlElement
    public Auteur getAuteur() {
        return auteur;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }

    @XmlElement
    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    @XmlElement
    public int getParution() {
        return parution;
    }

    public void setParution(int parution) {
        this.parution = parution;
    }

    @XmlElement
    public int getColonne() {
        return colonne;
    }

    public void setColonne(int colonne) {
        this.colonne = colonne;
    }

    @XmlElement
    public int getRangee() {
        return rangee;
    }

    public void setRangee(int rangee) {
        this.rangee = rangee;
    }
}
