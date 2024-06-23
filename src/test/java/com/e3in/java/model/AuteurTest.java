package com.e3in.java.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuteurTest {

    private Auteur auteur;

    @BeforeEach
    void setUp() {
        auteur = new Auteur();
        auteur.setNom("Nom");
        auteur.setPrenom("Prenom");
    }

    @Test
    void testEquals() {
        Auteur auteurB = new Auteur("Nom", "Prenom");
        assertEquals(auteur, auteurB);
    }

    @Test
    void testGetSetNom() {
        auteur.setNom("Nom");
        assertEquals("Nom", auteur.getNom());
    }

    @Test
    void testGetSetPrenom() {
        auteur.setPrenom("Prenom");
        assertEquals("Prenom", auteur.getPrenom());
    }

    @Test
    void testToString() {
        assertEquals("Prenom Nom", auteur.toString());
    }

    @Test
    void testToXml() {
        assertEquals("<Auteur>\n<nom>Nom</nom>\n<prenom>Prenom</prenom>\n</Auteur>\n", auteur.toXml());
    }
}