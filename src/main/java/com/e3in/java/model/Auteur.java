package com.e3in.java.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

/**
 * Représente un auteur d'un livre.
 */
@XmlRootElement(name = "auteur")
public class Auteur {
    private String nom;
    private String prenom;

    /**
     * Constructeur par défaut de l'auteur.
     */
    public Auteur() { }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Auteur auteur) {
            return (auteur.getNom().equals(this.getNom()) && auteur.getPrenom().equals(this.getPrenom()));
        } else {
            return super.equals(obj);
        }
    }

    /**
     * Retourne un code de hachage pour cet objet Auteur.
     *
     * @return un code de hachage pour cet objet Auteur.
    */
    @Override
    public int hashCode() {
        return Objects.hash(nom, prenom);
    }

    /**
     * Constructeur de l'auteur avec son nom et son prénom.
     *
     * @param nom    Le nom de l'auteur.
     * @param prenom Le prénom de l'auteur.
     */
    public Auteur(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    /**
     * Récupère le nom de l'auteur.
     *
     * @return Le nom de l'auteur.
     */
    @XmlElement
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de l'auteur.
     *
     * @param nom Le nom de l'auteur.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Récupère le prénom de l'auteur.
     *
     * @return Le prénom de l'auteur.
     */
    @XmlElement
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit le prénom de l'auteur.
     *
     * @param prenom Le prénom de l'auteur.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Retourne le nom complet de l'auteur.
     *
     * @return Le nom complet de l'auteur.
     */
    @Override
    public String toString() {
        return this.prenom + " " + this.nom;
    }

    /**
     * Retourne une représentation XML de l'auteur.
     *
     * @return L'auteur au format XML.
     */
    public String toXml() {
        String myStr = "<Auteur>\n";
        myStr += "<nom>" + this.nom + "</nom>\n";
        myStr += "<prenom>" + this.prenom + "</prenom>\n";
        myStr += "</Auteur>\n";
        return myStr;
    }
}
