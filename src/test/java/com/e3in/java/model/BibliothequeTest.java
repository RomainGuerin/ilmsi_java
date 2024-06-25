package com.e3in.java.model;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BibliothequeTest {

    private Bibliotheque bibliotheque;

    @BeforeEach
    void setUp() {
        bibliotheque = new Bibliotheque();
    }

    @Test
    @Order(1)
    void testGetLivres() {
        assertEquals(0, bibliotheque.getLivres().size());
    }

    @Test
    @Order(2)
    void testSetLivres() {
        bibliotheque.setLivres(List.of(getLivre(1)));
        assertEquals(1, bibliotheque.getLivres().size());
        assertEquals("Titre_1", bibliotheque.getLivres().get(0).getTitre());
    }

    @Test
    @Order(3)
    void testBiblioContainLivre() {
        Livre livre = getLivre(1);
        assertFalse(bibliotheque.contain(livre));
        bibliotheque.setLivres(List.of(livre));
        assertTrue(bibliotheque.contain(livre));
    }

    @Test
    @Order(4)
    void testCompareNewBibliotheque() {
        Bibliotheque newBibliotheque = new Bibliotheque(List.of(getLivre(2)));
        assertNotEquals(bibliotheque.getLivres(), newBibliotheque.getLivres());
    }

    Livre getLivre(int id) {
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