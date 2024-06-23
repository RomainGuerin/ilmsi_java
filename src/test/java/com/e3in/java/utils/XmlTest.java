package com.e3in.java.utils;

import com.e3in.java.model.Auteur;
import com.e3in.java.model.Bibliotheque;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XmlTest {

    @Test
    void testValidateXml_validXml() {
        String xmlFilePath = "src/test/resources/xmlDAO.xml";
        assertTrue(Xml.validateXml(xmlFilePath));
    }

    @Test
    void testValidateXml_invalidXml() {
        String xmlFilePath = "src/test/resources/xmlDAO_invalid.xml";
        assertFalse(Xml.validateXml(xmlFilePath));
    }

    @Test
    @Disabled
    void testBuildLibraryFromXML() {
        String xmlFilePath = "src/test/resources/xmlDAO.xml";
        Bibliotheque bibliotheque = Xml.buildLibraryFromXML(xmlFilePath);
        assertNotNull(bibliotheque);
        Auteur auteur = bibliotheque.getLivres().get(0).getAuteur();
        assertNotNull(auteur);
        assertEquals("Jean Dupond", auteur.toString());
    }
}
