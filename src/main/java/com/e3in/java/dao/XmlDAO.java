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

    /**
     * Sélectionne une entrée unique dans le fichier XML.
     *
     * @param tableName Le nom de la table à interroger.
     * @param columnNames Les colonnes à sélectionner.
     * @param whereClause La clause WHERE pour filtrer les résultats.
     * @return Une HashMap contenant les valeurs des colonnes sélectionnées.
     */
    @Override
    public HashMap<String, String> select(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        HashMap<String, String> result = new HashMap<>();

        try {
            // Initialisation des objets pour la lecture du fichier XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));

            // Récupération de la liste des éléments du type "tableName"
            NodeList nodeList = doc.getElementsByTagName(tableName);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Vérification des conditions de la clause WHERE
                    boolean match = true;
                    for (String key : whereClause.keySet()) {
                        if (!element.getElementsByTagName(key).item(0).getTextContent().equals(whereClause.get(key))) {
                            match = false;
                            break;
                        }
                    }

                    // Si les conditions sont remplies, extraction des valeurs des colonnes demandées
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

    /**
     * Sélectionne plusieurs entrées dans le fichier XML.
     *
     * @param tableName Le nom de la table à interroger.
     * @param columnNames Les colonnes à sélectionner.
     * @param whereClause La clause WHERE pour filtrer les résultats.
     * @return Une ArrayList contenant les HashMaps avec les valeurs des colonnes sélectionnées.
     */
    @Override
    public ArrayList<HashMap<String, String>> selectAll(String tableName, List<String> columnNames, HashMap<String, String> whereClause) {
        ArrayList<HashMap<String, String>> results = new ArrayList<>();

        try {
            // Initialisation des objets pour la lecture du fichier XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));

            // Récupération de la liste des éléments du type "tableName"
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

    /**
     * Insère une nouvelle entrée dans le fichier XML.
     *
     * @param tableName Le nom de la table où insérer les données.
     * @param columnAndValue Les colonnes et leurs valeurs à insérer.
     * @return Une HashMap contenant le statut de l'insertion.
     */
    @Override
    public HashMap<String, String> insert(String tableName, LinkedHashMap<String, String> columnAndValue) {
        HashMap<String, String> result = new HashMap<>();

        try {
            // Initialisation des objets pour la lecture du fichier XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));

            // Création d'un nouvel élément et ajout des valeurs des colonnes
            Element root = doc.getDocumentElement();
            Element newElement = doc.createElement(tableName);

            for (Map.Entry<String, String> entry : columnAndValue.entrySet()) {
                Element element = doc.createElement(entry.getKey());
                element.appendChild(doc.createTextNode(entry.getValue()));
                newElement.appendChild(element);
            }

            // Ajout du nouvel élément au document XML et sauvegarde du fichier
            root.appendChild(newElement);
            saveXML(doc);

            result.put(Constants.STATUS, Constants.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(Constants.STATUS, Constants.ERROR);
        }

        return result;
    }

    /**
     * Met à jour une entrée existante dans le fichier XML.
     *
     * @param tableName Le nom de la table à mettre à jour.
     * @param columnAndValue Les colonnes et leurs nouvelles valeurs.
     * @param whereClause La clause WHERE pour filtrer les résultats.
     * @return Une HashMap contenant le statut de la mise à jour.
     */
    @Override
    public HashMap<String, String> update(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause) {
        HashMap<String, String> result = new HashMap<>();

        try {
            // Initialisation des objets pour la lecture du fichier XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));

            // Récupération de la liste des éléments du type "tableName"
            NodeList nodeList = doc.getElementsByTagName(tableName);
            boolean updated = false;

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Vérification des conditions de la clause WHERE
                    boolean match = true;
                    for (String key : whereClause.keySet()) {
                        if (!element.getElementsByTagName(key).item(0).getTextContent().equals(whereClause.get(key))) {
                            match = false;
                            break;
                        }
                    }

                    // Si les conditions sont remplies, mise à jour des valeurs des colonnes
                    if (match) {
                        for (String key : columnAndValue.keySet()) {
                            element.getElementsByTagName(key).item(0).setTextContent(columnAndValue.get(key));
                        }
                        updated = true;
                        break;
                    }
                }
            }

            // Sauvegarde du fichier XML si une mise à jour a été effectuée
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

    /**
     * Supprime une entrée dans le fichier XML.
     *
     * @param tableName Le nom de la table d'où supprimer les données.
     * @param whereClause La clause WHERE pour filtrer les résultats.
     * @return Une HashMap contenant le statut de la suppression.
     */
    @Override
    public HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause) {
        HashMap<String, String> result = new HashMap<>();

        try {
            // Initialisation des objets pour la lecture du fichier XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));

            // Récupération de la liste des éléments du type "tableName"
            NodeList nodeList = doc.getElementsByTagName(tableName);
            boolean deleted = false;

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Vérification des conditions de la clause WHERE
                    boolean match = true;
                    for (String key : whereClause.keySet()) {
                        if (!element.getElementsByTagName(key).item(0).getTextContent().equals(whereClause.get(key))) {
                            match = false;
                            break;
                        }
                    }

                    // Si les conditions sont remplies, suppression de l'élément
                    if (match) {
                        element.getParentNode().removeChild(element);
                        deleted = true;
                        break;
                    }
                }
            }

            // Sauvegarde du fichier XML si une suppression a été effectuée
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

    /**
     * Sauvegarde le document XML dans le fichier.
     *
     * @param doc Le document XML à sauvegarder.
     * @throws TransformerException Si une erreur survient lors de la transformation du document.
     */
    private void saveXML(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File(xmlFilePath));
        transformer.transform(domSource, streamResult);
    }
}
