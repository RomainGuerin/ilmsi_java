package com.e3in.java.dao;

import com.e3in.java.utils.Constants;
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
import java.util.*;

// TODO : Refactor le xml DAO parce que c'est moche mais genre vraiment
public class XmlDAO implements DAO {
    private String xmlFilePath;

    public XmlDAO() {
        this.xmlFilePath = "";
    }

    public XmlDAO(String xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    public void setXmlFilePath(String xmlFilePath) {
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
    public ArrayList<HashMap<String, String>> selectAll(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        ArrayList<HashMap<String, String>> results = new ArrayList<>();

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
                        Node item = element.getElementsByTagName(key).item(0);
                        if (item == null || !item.getTextContent().equals(whereClause.get(key))) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        HashMap<String, String> result = new HashMap<>();
                        for (String column : columnNames) {
                            Node item = element.getElementsByTagName(column).item(0);
                            if (item != null) {
                                result.put(column, item.getTextContent());
                            }
                        }
                        results.add(result);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }



    @Override
    public HashMap<String, String> insert(String tableName, LinkedHashMap<String, String> columnAndValue) {
        HashMap<String, String> result = new HashMap<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));

            Element root = doc.getDocumentElement();
            Element newElement = doc.createElement(tableName);

            for (Map.Entry<String, String> entry : columnAndValue.entrySet()) {
                Element element = doc.createElement(entry.getKey());
                element.appendChild(doc.createTextNode(entry.getValue()));
                newElement.appendChild(element);
            }

            root.appendChild(newElement);
            saveXML(doc);

            result.put(Constants.STATUS, Constants.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(Constants.STATUS, Constants.ERROR);
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
                result.put(Constants.STATUS, Constants.SUCCESS);
            } else {
                result.put(Constants.STATUS, Constants.FAILURE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put(Constants.STATUS, Constants.ERROR);
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
                result.put(Constants.STATUS, Constants.SUCCESS);
            } else {
                result.put(Constants.STATUS, Constants.FAILURE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put(Constants.STATUS, Constants.ERROR);
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