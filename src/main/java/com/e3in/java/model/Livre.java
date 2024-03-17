package com.e3in.java.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "livre")
@XmlType(propOrder = {"titre", "auteur", "presentation", "parution", "colonne", "rangee", "jaquette"})
public class Livre {
    private String titre;
    private Auteur auteur;
    private String presentation;
    private int parution;
    private int colonne;
    private int rangee;
    private String jaquette;

    @XmlElement
    public String getTitre() {
        return this.titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @XmlElement
    public Auteur getAuteur() {
        return this.auteur;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }
    public void setAuteur(String auteur) {
        String[] temp = auteur.split(" ");
        if(temp.length < 1) {
            //error
        } else if(temp.length > 1) {
            this.auteur.setPrenom(temp[0].strip());
            this.auteur.setNom(temp[1].strip());
        } else {
            this.auteur.setPrenom(temp[0].strip());
            this.auteur.setNom(temp[0].strip());
        }
    }

    @XmlElement
    public String getPresentation() {
        return this.presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    @XmlElement
    public int getParution() {
        return this.parution;
    }

    public void setParution(int parution) {
        this.parution = parution;
    }

    @XmlElement
    public int getColonne() {
        return this.colonne;
    }

    public void setColonne(int colonne) {
        this.colonne = colonne;
    }

    @XmlElement
    public int getRangee() {
        return this.rangee;
    }

    public void setRangee(int rangee) {
        this.rangee = rangee;
    }
    @XmlElement
    public String getJaquette() {
        return this.jaquette;
    }

    public void setJaquette(String jaquette) {
        this.jaquette = jaquette;
    }

    @Override
    public String toString() {
        return this.toXml();
    }
    public String toXml() {
        String myStr = "<Livre>\n";
        myStr += "<Titre>"+this.getTitre()+"</Titre>\n";
        myStr += this.getAuteur().toXml();
        myStr += "<Presentation>"+this.getPresentation()+"</Presentation>\n";
        myStr += "<Parution>"+this.getParution()+"</Parution>\n";
        myStr += "<Colonne>"+this.getColonne()+"</Colonne>\n";
        myStr += "</Livre>\n";
        return myStr;
    }
}
