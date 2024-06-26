package com.e3in.java.dao;

import com.e3in.java.model.Auteur;
import com.e3in.java.model.Bibliotheque;
import com.e3in.java.model.Livre;
import com.e3in.java.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BibliothequeDAOTest {

    @Mock
    private DAOManager daoManager;

    private BibliothequeDAO bibliothequeDAO;

    private Livre basicLivre;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bibliothequeDAO = new BibliothequeDAO(daoManager);
        basicLivre = getLivre();
    }

    @Test
    void testGetAllBibliotheque() {
        List<String> columnNames = List.of(
                Constants.TITRE,
                Constants.AUTEUR_NOM,
                Constants.AUTEUR_PRENOM,
                Constants.PRESENTATION,
                Constants.JAQUETTE,
                Constants.PARUTION,
                Constants.COLONNE,
                Constants.RANGEE,
                Constants.EMPRUNTE
        );

        ArrayList<HashMap<String, String>> resultQuery = getHashMapLivre();

        when(daoManager.selectAll(Constants.TABLE_LIVRE, columnNames, new HashMap<>())).thenReturn(resultQuery);

        Bibliotheque bibliotheque = bibliothequeDAO.getAllBibliotheque();

        assertNotNull(bibliotheque);
        assertEquals(1, bibliotheque.getLivres().size());
        assertEquals("Test Livre", bibliotheque.getLivres().get(0).getTitre());
    }

    @Test
    void testAddLivreBibliotheque() {
        HashMap<String, String> resultQuery = new HashMap<>();
        resultQuery.put(Constants.STATUS, Constants.SUCCESS);

        when(daoManager.insert(eq(Constants.TABLE_LIVRE), any(LinkedHashMap.class))).thenReturn(resultQuery);

        boolean result = bibliothequeDAO.addLivreBibliotheque(basicLivre);
        assertTrue(result);
    }

    @Test
    void testUpdateLivreBibliotheque() {
        HashMap<String, String> resultQuery = new HashMap<>();
        resultQuery.put(Constants.STATUS, Constants.SUCCESS);

        when(daoManager.update(eq(Constants.TABLE_LIVRE), any(HashMap.class), any(HashMap.class))).thenReturn(resultQuery);

        boolean result = bibliothequeDAO.updateLivreBibliotheque(basicLivre);
        assertTrue(result);
    }

    @Test
    void testRemoveLivreBibliotheque() {
        HashMap<String, String> resultQuery = new HashMap<>();
        resultQuery.put(Constants.STATUS, Constants.SUCCESS);

        when(daoManager.delete(eq(Constants.TABLE_LIVRE), any(HashMap.class))).thenReturn(resultQuery);

        boolean result = bibliothequeDAO.removeLivreBibliotheque(basicLivre);
        assertTrue(result);
    }

    private static ArrayList<HashMap<String, String>> getHashMapLivre() {
        ArrayList<HashMap<String, String>> resultQuery = new ArrayList<>();
        HashMap<String, String> livreData = new HashMap<>();
        livreData.put(Constants.TITRE, "Test Livre");
        livreData.put(Constants.AUTEUR_NOM, "Doe");
        livreData.put(Constants.AUTEUR_PRENOM, "John");
        livreData.put(Constants.PRESENTATION, "Une présentation");
        livreData.put(Constants.JAQUETTE, "Une jaquette");
        livreData.put(Constants.PARUTION, "2020");
        livreData.put(Constants.COLONNE, "1");
        livreData.put(Constants.RANGEE, "1");
        livreData.put(Constants.EMPRUNTE, "false");
        resultQuery.add(livreData);
        return resultQuery;
    }

    private Livre getLivre() {
        Livre livre = new Livre();
        livre.setTitre("Test Livre");
        livre.setAuteur(new Auteur("Doe", "John"));
        livre.setPresentation("Une nouvelle présentation");
        livre.setJaquette("Une nouvelle jaquette");
        livre.setParution(2020);
        livre.setColonne(1);
        livre.setRangee(1);
        livre.setEmprunte(false);
        return livre;
    }
}
