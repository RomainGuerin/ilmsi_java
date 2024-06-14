package com.e3in.java.dao;

import com.e3in.java.model.Auteur;
import com.e3in.java.model.Bibliotheque;
import com.e3in.java.model.Livre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BibliothequeDAO {
    private final DAOManager daoManager;

    public BibliothequeDAO(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    public Bibliotheque getAllBibliotheque() {
        ArrayList<HashMap<String, String>> resultQuery = daoManager.selectAll("livre", List.of("titre", "auteurNom", "auteurPrenom","presentation", "jaquette", "parution", "colonne", "rangee", "emprunte"), new HashMap<>());
        return getBibliotheque(resultQuery);
    }

    public boolean addLivreBibliotheque(Livre livre) {
        HashMap<String, String> map = getMapLivre(livre);
        HashMap<String, String> mapLivreUnique = getMapLivreUnique(livre);
        map.putAll(mapLivreUnique);

        HashMap<String, String> resultQuery = daoManager.insert("livre", map);
        return !resultQuery.isEmpty() && resultQuery.get("status").equals("success");
    }

    public boolean updateLivreBibliotheque(Livre livre) {
        HashMap<String, String> resultQuery = daoManager.update("livre", getMapLivre(livre), getMapLivreUnique(livre));
        return !resultQuery.isEmpty() && resultQuery.get("status").equals("success");
    }

    public boolean removeLivreBibliotheque(Livre livre) {
        HashMap<String, String> resultQuery = daoManager.delete("livre", getMapLivreUnique(livre));
        return !resultQuery.isEmpty() && resultQuery.get("status").equals("success");
    }

    private static Bibliotheque getBibliotheque(ArrayList<HashMap<String, String>> resultQuery) {
        Bibliotheque bibliotheque = new Bibliotheque();
        for (HashMap<String, String> map : resultQuery) {
            Livre livre = new Livre();
            livre.setTitre(map.get("titre"));
            livre.setAuteur(new Auteur(map.get("auteurNom"), map.get("auteurPrenom")));
            livre.setPresentation(map.get("presentation"));
            livre.setJaquette(map.get("jaquette"));
            livre.setParution(Integer.parseInt(map.get("parution")));
            livre.setColonne(Integer.parseInt(map.get("colonne")));
            livre.setRangee(Integer.parseInt(map.get("rangee")));
            livre.setEmprunte(Boolean.parseBoolean(map.get("emprunte")));
            bibliotheque.getLivres().add(livre);
        }
        return bibliotheque;
    }

    private HashMap<String, String> getMapLivre(Livre livre) {
        HashMap<String, String> map = new HashMap<>();
        map.put("presentation", livre.getPresentation());
        map.put("jaquette", livre.getJaquette());
        map.put("colonne", String.valueOf(livre.getColonne()));
        map.put("rangee", String.valueOf(livre.getRangee()));
        map.put("emprunte", livre.getEmprunte() ? "1" : "0");
        return map;
    }

    private HashMap<String, String> getMapLivreUnique(Livre livre) {
        HashMap<String, String> mapLivreUnique = new HashMap<>();
        mapLivreUnique.put("titre", livre.getTitre());
        mapLivreUnique.put("auteurNom", livre.getAuteur().getNom());
        mapLivreUnique.put("auteurPrenom", livre.getAuteur().getPrenom());
        mapLivreUnique.put("parution", String.valueOf(livre.getParution()));
        return mapLivreUnique;
    }
}
