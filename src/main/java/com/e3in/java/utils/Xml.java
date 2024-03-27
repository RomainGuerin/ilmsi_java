package com.e3in.java.utils;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.e3in.java.model.Bibliotheque;
import com.e3in.java.model.Livre;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Classe utilitaire pour la manipulation d'XML.
 */
public class XmlUtils {
    // Chemin vers le fichier XSD utilisé pour la validation XML
    private static final String xsdFilePath = Objects.requireNonNull(XmlUtils.class.getResource("/Biblio.xsd")).getPath();

    /**
     * Valide un fichier XML par rapport à un schéma XSD.
     *
     * @param xmlFilePath Chemin vers le fichier XML à valider.
     * @return True si le fichier XML est valide, sinon False.
     */
    public static boolean validateXml(String xmlFilePath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(XmlUtils.xsdFilePath));
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

    /**
     * Enregistre une liste de livres dans un fichier XML.
     *
     * @param livres   Liste des livres à enregistrer.
     * @param filePath Chemin vers le fichier XML de sortie.
     */
    public static void saveLibraryToXml(List<Livre> livres, String filePath) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Bibliotheque.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Bibliotheque library = new Bibliotheque();
            library.setLivres(livres);

            marshaller.marshal(library, new File(filePath));
        } catch (JAXBException e) {
            System.err.println("Erreur d'enregistrement du XML : " + e.getMessage());
        }
    }
}
