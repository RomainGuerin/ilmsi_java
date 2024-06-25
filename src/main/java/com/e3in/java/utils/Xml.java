package com.e3in.java.utils;

import com.e3in.java.model.Bibliotheque;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Classe utilitaire pour la manipulation d'XML.
 */
public class Xml {
    private static final String XSD_NAME = "Biblio.xsd";
    private static final String XSD_PATH = System.getProperty("user.home") + File.separator + XSD_NAME;

    static Logger logger = Logger.getLogger(Xml.class.getName());

    /**
     * Valide un fichier XML par rapport à un schéma XSD.
     *
     * @param xmlFilePath Chemin vers le fichier XML à valider.
     * @return True si le fichier XML est valide, sinon False.
     */
    public static boolean validateXml(String xmlFilePath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // Configuration sécurisée de la factory
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            // Chargement du schéma
            Schema schema = factory.newSchema(Common.createOrGetFile(XSD_NAME, XSD_PATH, Xml.class));
            Validator validator = schema.newValidator();

            // Validation du fichier XML
            Source source = new StreamSource(new File(xmlFilePath));
            validator.validate(source);

            return true;
        } catch (SAXNotRecognizedException | SAXNotSupportedException | IllegalArgumentException e) {
            logger.severe("Erreur de configuration de la SchemaFactory : " + e.getMessage());
            return false;
        } catch (SAXException | IOException e) {
            logger.severe("Erreur de validation XML : " + e.getMessage());
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
