package com.e3in.java.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.logging.Logger;

/**
 * Représente un livre avec ses attributs tels que le titre, l'auteur, la présentation, etc.
 */
@XmlRootElement(name = "livre")
@XmlType(propOrder = { "titre", "auteur", "presentation", "jaquette", "parution", "colonne", "rangee", "emprunte"})
public class Livre {
    static Logger logger = Logger.getLogger(Livre.class.getName());
    private String titre;
    private Auteur auteur;
    private String presentation;
    private String jaquette;
    private int parution;
    private int colonne;
    private int rangee;
    private boolean emprunte;

    /**
     * Constructeur par défaut du livre.
     */
    public Livre() { }

    /**
     * Constructeur du livre avec ses attributs.
     * 
     * @param titre        Le titre du livre.
     * @param auteur       L'auteur du livre.
     * @param presentation La présentation du livre.
     * @param jaquette     La jaquette du livre.
     * @param parution     L'année de parution du livre.
     * @param colonne      Le numéro de colonne du livre.
     * @param rangee       Le numéro de la rangée du livre.
     */
    public Livre(String titre, Auteur auteur, String presentation, String jaquette, int parution, int colonne, int rangee, boolean emprunte) {
        this.titre = titre;
        this.auteur = auteur;
        this.presentation = presentation;
        this.jaquette = jaquette;
        this.parution = parution;
        this.colonne = colonne;
        this.rangee = rangee;
        this.emprunte = emprunte;
    }

    /**
     * Récupère le titre du livre.
     * 
     * @return Le titre du livre.
     */
    @XmlElement
    public String getTitre() {
        return this.titre;
    }

    /**
     * Définit le titre du livre.
     * 
     * @param titre Le titre du livre.
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Récupère l'auteur du livre.
     * 
     * @return L'auteur du livre.
     */
    @XmlElement
    public Auteur getAuteur() {
        return this.auteur;
    }

    /**
     * Définit l'auteur du livre à partir d'un objet Auteur.
     * 
     * @param auteur L'auteur du livre sous forme d'objet Auteur.
     */
    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }

    /**
     * Définit l'auteur du livre à partir de son nom complet.
     * Si le nom est invalide, une exception est levée.
     * 
     * @param auteur Le nom complet de l'auteur.
     */
    public void setAuteur(String auteur) {
        if (this.auteur == null) {
            this.auteur = new Auteur();
        }
        String[] temp = auteur.split(" ");
        if (temp.length < 1) {
            try {
                throw new Exception("Auteur invalide");
            } catch (Exception e) {
                logger.severe("Auteur invalide : " + e.getMessage());
            }
        } else if (temp.length > 1) {
            this.auteur.setPrenom(temp[0].strip());
            this.auteur.setNom(temp[1].strip());
        } else {
            this.auteur.setPrenom(temp[0].strip());
            this.auteur.setNom(temp[0].strip());
        }
    }

    /**
     * Récupère la présentation du livre.
     * 
     * @return La présentation du livre.
     */
    @XmlElement
    public String getPresentation() {
        return this.presentation;
    }

    /**
     * Définit la présentation du livre.
     * 
     * @param presentation La présentation du livre.
     */
    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    /**
     * Récupère la jaquette du livre.
     * 
     * @return La jaquette du livre.
     */
    @XmlElement
    public String getJaquette() {
        return this.jaquette;
    }

    /**
     * Définit la jaquette du livre.
     * 
     * @param jaquette La jaquette du livre.
     */
    public void setJaquette(String jaquette) {
        this.jaquette = jaquette;
    }

    /**
     * Récupère l'année de parution du livre.
     * 
     * @return L'année de parution du livre.
     */
    @XmlElement
    public int getParution() {
        return this.parution;
    }

    /**
     * Définit l'année de parution du livre.
     * 
     * @param parution L'année de parution du livre.
     */
    public void setParution(int parution) {
        this.parution = parution;
    }

    /**
     * Récupère le numéro de colonne du livre.
     * 
     * @return Le numéro de colonne du livre.
     */
    @XmlElement
    public int getColonne() {
        return this.colonne;
    }

    /**
     * Définit le numéro de colonne du livre.
     * 
     * @param colonne Le numéro de colonne du livre.
     */
    public void setColonne(int colonne) {
        this.colonne = colonne;
    }

    /**
     * Récupère le numéro de la rangée du livre.
     * 
     * @return Le numéro de la rangée du livre.
     */
    @XmlElement
    public int getRangee() {
        return this.rangee;
    }

    /**
     * Définit le numéro de la rangée du livre.
     * 
     * @param rangee Le numéro de la rangée du livre.
     */
    public void setRangee(int rangee) {
        this.rangee = rangee;
    }

    @XmlElement
    public boolean getEmprunte() {
        return this.emprunte;
    }

    public String getEmprunteString() {
        if (this.emprunte) {
            return "Emprunté";
        } else {
            return "Disponible";
        }
    }

    /**
     * Définit l'état d'emprunt du livre.
     *
     * @param emprunte L'état d'emprunt du livre.
     */
    public void setEmprunte(boolean emprunte) {
        this.emprunte= emprunte;
    }

    /**
     * Retourne une représentation XML du livre.
     * 
     * @return Le livre au format XML.
     */
    @Override
    public String toString() {
        return this.toXml();
    }

    /**
     * Retourne une représentation XML du livre.
     * 
     * @return Le livre au format XML.
     */
    public String toXml() {
        String myStr = "<Livre>\n";
        myStr += "<Titre>" + this.getTitre() + "</Titre>\n";
        myStr += this.getAuteur().toXml();
        myStr += "<Presentation>" + this.getPresentation() + "</Presentation>\n";
        myStr += "<Jaquette>" + this.getJaquette() + "</Jaquette>\n";
        myStr += "<Parution>" + this.getParution() + "</Parution>\n";
        myStr += "<Colonne>" + this.getColonne() + "</Colonne>\n";
        myStr += "<Emprunte>" + this.getEmprunte() + "</Emprunte>\n";
        myStr += "</Livre>\n";
        return myStr;
    }
}
