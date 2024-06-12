package com.e3in.java.dao;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public class XMLDAO implements DAO {
    private final String xmlFilePath;

    public XMLDAO(String xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    @Override
    public HashMap<String, String> select(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        HashMap<String, String> result = new HashMap<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));

            NodeList nodeList = doc.getElementsByTagName(tableName);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    boolean match = true;
                    for (String key : whereClause.keySet()) {
                        if (!element.getElementsByTagName(key).item(0).getTextContent().equals(whereClause.get(key))) {
                            match = false;
                            break;
                        }
                    }

                    if (match) {
                        for (String column : columnNames) {
                            result.put(column, element.getElementsByTagName(column).item(0).getTextContent());
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public HashMap<String, String> insert(String tableName, HashMap<String, String> columnAndValue) {
        HashMap<String, String> result = new HashMap<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));

            Element root = doc.getDocumentElement();
            Element newElement = doc.createElement(tableName);

            for (String key : columnAndValue.keySet()) {
                Element element = doc.createElement(key);
                element.appendChild(doc.createTextNode(columnAndValue.get(key)));
                newElement.appendChild(element);
            }

            root.appendChild(newElement);
            saveXML(doc);

            result.put("status", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
        }

        return result;
    }

    @Override
    public HashMap<String, String> update(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause) {
        HashMap<String, String> result = new HashMap<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));

            NodeList nodeList = doc.getElementsByTagName(tableName);
            boolean updated = false;

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    boolean match = true;
                    for (String key : whereClause.keySet()) {
                        if (!element.getElementsByTagName(key).item(0).getTextContent().equals(whereClause.get(key))) {
                            match = false;
                            break;
                        }
                    }

                    if (match) {
                        for (String key : columnAndValue.keySet()) {
                            element.getElementsByTagName(key).item(0).setTextContent(columnAndValue.get(key));
                        }
                        updated = true;
                        break;
                    }
                }
            }

            if (updated) {
                saveXML(doc);
                result.put("status", "success");
            } else {
                result.put("status", "failure");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
        }

        return result;
    }

    @Override
    public HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause) {
        HashMap<String, String> result = new HashMap<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));

            NodeList nodeList = doc.getElementsByTagName(tableName);
            boolean deleted = false;

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    boolean match = true;
                    for (String key : whereClause.keySet()) {
                        if (!element.getElementsByTagName(key).item(0).getTextContent().equals(whereClause.get(key))) {
                            match = false;
                            break;
                        }
                    }

                    if (match) {
                        element.getParentNode().removeChild(element);
                        deleted = true;
                        break;
                    }
                }
            }

            if (deleted) {
                saveXML(doc);
                result.put("status", "success");
            } else {
                result.put("status", "failure");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "error");
        }

        return result;
    }

    private void saveXML(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File(xmlFilePath));
        transformer.transform(domSource, streamResult);
    }
}
