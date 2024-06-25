package com.e3in.java.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LivreTest {

    private Livre livre;

    @BeforeEach
    void setUp() {
        livre = new Livre();
    }

    @Test
    void testEquals() {
        Livre livre1 = createLivre(1);
        Livre livre2 = createLivre(1);

        assertEquals(livre1, livre2);
    }

    @Test
    void testHashCode() {
        Livre livre1 = createLivre(1);
        assertNotEquals(0, livre1.hashCode());
    }

    @Test
    void testGetSetTitre() {
        livre.setTitre("Title");
        String titre = livre.getTitre();
        assertEquals("Title", titre);
    }

    @Test
    void testGetSetAuteur() {
        livre.setAuteur("Nom Prenom");
        Auteur retrievedAuteur = livre.getAuteur();
        assertEquals("Nom Prenom", retrievedAuteur.toString());
    }

    @Test
    void testGetSetPresentation() {
        livre.setPresentation("Presentation");
        String presentation = livre.getPresentation();
        assertEquals("Presentation", presentation);
    }

    @Test
    void testGetSetJaquette() {
        livre.setJaquette("Jaquette");
        String jaquette = livre.getJaquette();
        assertEquals("Jaquette", jaquette);
    }

    @Test
    void testGetSetParution() {
        livre.setParution(2022);
        int parution = livre.getParution();
        assertEquals(2022, parution);
    }

    @Test
    void testGetSetColonne() {
        livre.setColonne(1);
        int colonne = livre.getColonne();
        assertEquals(1, colonne);
    }

    @Test
    void testGetSetRangee() {
        livre.setRangee(1);
        int rangee = livre.getRangee();
        assertEquals(1, rangee);
    }

    @Test
    void testGetSetEmprunte() {
        livre.setEmprunte(false);
        boolean emprunte = livre.getEmprunte();
        assertFalse(emprunte);
    }

    @Test
    void testToStringAndXML() {
        Livre livre1 = createLivre(0);

        String livreString = livre1.toString();
        String livretoXml = livre1.toXml();

        assertEquals("""
            <Livre>
            <Titre>Titre_0</Titre>
            <Auteur>
            <nom>Nom</nom>
            <prenom>Prenom</prenom>
            </Auteur>
            <Presentation>Presentation</Presentation>
            <Jaquette>Jaquette</Jaquette>
            <Parution>2000</Parution>
            <Colonne>1</Colonne>
            <Emprunte>true</Emprunte>
            </Livre>
            """, livreString);
        assertEquals(livreString, livretoXml);
    }

    Livre createLivre(int id) {
        return new Livre(
                "Titre_" + id,
                new Auteur("Nom", "Prenom"),
                "Presentation",
                "Jaquette",
                2000,
                1,
                1,
                true
        );
    }
}
