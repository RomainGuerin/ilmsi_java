package com.e3in.java.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Map;
import java.util.logging.Level;
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

    public Document getDocument() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new File(xmlFilePath));
    }

    public void saveDocument(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File(xmlFilePath));
        transformer.transform(domSource, streamResult);
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

