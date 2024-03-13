package com.e3in.java.controller;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

public class XmlValidator {
    private static String xsdFilePath = XmlValidator.class.getClass().getResource("/Biblio.xsd").getPath();
    public static boolean validateXml(String xmlFilePath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(XmlValidator.xsdFilePath));
            Validator validator = schema.newValidator();

            Source source = new StreamSource(new File(xmlFilePath));
            validator.validate(source);

            return true;
        } catch (SAXException | IOException e) {
            System.err.println("Erreur de validation XML : " + e.getMessage());
            return false;
        }
    }
}
