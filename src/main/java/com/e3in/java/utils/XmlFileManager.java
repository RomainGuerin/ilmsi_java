package com.e3in.java.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class XmlFileManager {
    private static final Logger logger = Logger.getLogger(XmlFileManager.class.getName());
    private String xmlFilePath;

    public XmlFileManager(String xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    public void setXmlFilePath(String xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    public Document getDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Configuration sécurisée de DocumentBuilderFactory
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new File(xmlFilePath));
        } catch (SAXNotRecognizedException | SAXNotSupportedException | IllegalArgumentException |
                 ParserConfigurationException e) {
            logger.severe("Erreur de configuration de la DocumentBuilderFactory : " + e.getMessage());
        } catch (SAXException | IOException e) {
            logger.severe("Erreur de fichier XML XMLFile : " + e.getMessage());
        }
        return null;
    }

    public void saveDocument(Document doc) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            // Configuration sécurisée de TransformerFactory
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));
            transformer.transform(domSource, streamResult);
        } catch (IllegalArgumentException | TransformerException e) {
            logger.severe("Erreur de configuration de la TransformerFactory : " + e.getMessage());
        }
    }

    public boolean validateElement(Element element, Map<String, String> whereClause) {
        for (Map.Entry<String, String> entry : whereClause.entrySet()) {
            Node item = element.getElementsByTagName(entry.getKey()).item(0);
            if (item == null || !item.getTextContent().equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }
}

