package com.e3in.java.utils;

import com.e3in.java.model.Bibliotheque;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Classe utilitaire pour la manipulation d'XML.
 */
public class Xml{
    // Chemin vers le fichier XSD utilisé pour la validation XML
    private static final String xsdFilePath = Objects.requireNonNull(Xml.class.getResource("/Biblio.xsd")).getPath();

    /**
     * Valide un fichier XML par rapport à un schéma XSD.
     *
     * @param xmlFilePath Chemin vers le fichier XML à valider.
     * @return True si le fichier XML est valide, sinon False.
     */
    public static boolean validateXml(String xmlFilePath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(Xml.xsdFilePath));
            Validator validator = schema.newValidator();

            Source source = new StreamSource(new File(xmlFilePath));
            validator.validate(source);

            return true;
        } catch (SAXException | IOException e) {
            System.err.println("Erreur de validation XML : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Construit une bibliothèque de livres à partir d'un fichier XML.
     *
     * @param xmlFilePath Chemin vers le fichier XML contenant la bibliothèque.
     * @return La bibliothèque de livres construite à partir du fichier XML.
     */
    public static Bibliotheque buildLibraryFromXML(String xmlFilePath) {
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
