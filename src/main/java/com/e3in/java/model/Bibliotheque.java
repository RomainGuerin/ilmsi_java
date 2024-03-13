package com.e3in.java.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "bibliotheque")
public class Bibliotheque {
    private List<Livre> livres;
    
    @XmlElement(name = "livre")
    public List<Livre> getLivres() {
        return this.livres;
    }

    public void setLivres(List<Livre> livres) {
        this.livres = livres;
    }
}
