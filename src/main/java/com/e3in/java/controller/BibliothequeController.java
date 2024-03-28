package com.e3in.java.controller;

import java.io.File;
import java.util.List;

import com.e3in.java.model.Bibliotheque;
import com.e3in.java.model.Livre;
import com.e3in.java.utils.Xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class BibliothequeController {
    private Bibliotheque bibliotheque;

    public BibliothequeController(String xmlFilePath) {
        this.bibliotheque = buildLibraryFromXML(xmlFilePath);
    }

    public List<Livre> getBibliotheque() {
        return this.bibliotheque.getLivres();
    }

    /**
     * Construit une bibliothèque de livres à partir d'un fichier XML.
     *
     * @param xmlFilePath Chemin vers le fichier XML contenant la bibliothèque.
     * @return La bibliothèque de livres construite à partir du fichier XML.
     */
    private static Bibliotheque buildLibraryFromXML(String xmlFilePath) {
        try {
            File file = new File(xmlFilePath);
            JAXBContext jaxbContext = JAXBContext.newInstance(Bibliotheque.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            return (Bibliotheque) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            System.err.println("Erreur de chargement du XML : " + e.getMessage());
            return null;
        }
    }
}
