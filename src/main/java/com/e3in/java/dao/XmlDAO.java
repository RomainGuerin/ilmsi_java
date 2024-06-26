package com.e3in.java.dao;

import com.e3in.java.utils.Constants;
import com.e3in.java.utils.XmlFileManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.logging.Logger;

public class XmlDAO implements DAO {
    private static final Logger logger = Logger.getLogger(XmlDAO.class.getName());
    private final XmlFileManager xmlFileManager;

    public XmlDAO(String xmlFilePath) {
        this.xmlFileManager = new XmlFileManager(xmlFilePath);
    }

    public void setXmlFilePath(String xmlFilePath) {
        xmlFileManager.setXmlFilePath(xmlFilePath);
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
        try {
            Document doc = xmlFileManager.getDocument();
            Optional<Element> optionalElement = findMatchingElement(doc, tableName, whereClause);
            if (optionalElement.isPresent()) {
                return getElementData(optionalElement.get(), columnNames);
            } else {
                logger.warning("Aucune correspondance trouvée pour la sélection.");
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération des données : " + e.getMessage());
        }
        return new HashMap<>();
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
            Document doc = xmlFileManager.getDocument();
            NodeList nodeList = doc.getElementsByTagName(tableName);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (xmlFileManager.validateElement(element, whereClause)) {
                        results.add(getElementData(element, columnNames));
                    }
                }
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération des données : " + e.getMessage());
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
        try {
            validateInsertData(columnAndValue);
            Document doc = xmlFileManager.getDocument();

            Element newElement = createNewElement(doc, tableName, columnAndValue);
            doc.getDocumentElement().appendChild(newElement);

            xmlFileManager.saveDocument(doc);
            return createStatusMap(Constants.SUCCESS);
        } catch (Exception e) {
            logger.severe("Erreur lors de l'insertion des données : " + e.getMessage());
            return createStatusMap(Constants.ERROR);
        }
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
        try {
            validateUpdateData(columnAndValue, whereClause);
            Document doc = xmlFileManager.getDocument();

            Optional<Element> optionalElement = findMatchingElement(doc, tableName, whereClause);
            if (optionalElement.isPresent()) {
                updateElementData(optionalElement.get(), columnAndValue);
                xmlFileManager.saveDocument(doc);
                return createStatusMap(Constants.SUCCESS);
            } else {
                return createStatusMap(Constants.FAILURE);
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la mise à jour des données : " + e.getMessage());
            return createStatusMap(Constants.ERROR);
        }
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
        try {
            Document doc = xmlFileManager.getDocument();

            Optional<Element> optionalElement = findMatchingElement(doc, tableName, whereClause);
            if (optionalElement.isPresent()) {
                optionalElement.get().getParentNode().removeChild(optionalElement.get());
                xmlFileManager.saveDocument(doc);
                return createStatusMap(Constants.SUCCESS);
            } else {
                return createStatusMap(Constants.FAILURE);
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la suppression des données : " + e.getMessage());
            return createStatusMap(Constants.ERROR);
        }
    }

    private Optional<Element> findMatchingElement(Document doc, String tableName, Map<String, String> whereClause) {
        NodeList nodeList = doc.getElementsByTagName(tableName);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (xmlFileManager.validateElement(element, whereClause)) {
                    return Optional.of(element);
                }
            }
        }
        return Optional.empty();
    }

    private HashMap<String, String> getElementData(Element element, List<String> columnNames) {
        HashMap<String, String> result = new HashMap<>();
        for (String column : columnNames) {
            Node item = element.getElementsByTagName(column).item(0);
            if (item != null) {
                result.put(column, item.getTextContent());
            }
        }
        return result;
    }

    private Element createNewElement(Document doc, String tableName, LinkedHashMap<String, String> columnAndValue) {
        Element newElement = doc.createElement(tableName);
        for (Map.Entry<String, String> entry : columnAndValue.entrySet()) {
            Element element = doc.createElement(entry.getKey());
            element.appendChild(doc.createTextNode(entry.getValue()));
            newElement.appendChild(element);
        }
        return newElement;
    }

    private void updateElementData(Element element, Map<String, String> columnAndValue) {
        for (Map.Entry<String, String> entry : columnAndValue.entrySet()) {
            Node item = element.getElementsByTagName(entry.getKey()).item(0);
            if (item != null) {
                item.setTextContent(entry.getValue());
            }
        }
    }

    private void validateInsertData(Map<String, String> columnAndValue) throws IllegalArgumentException {
        for (Map.Entry<String, String> entry : columnAndValue.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                throw new IllegalArgumentException("Invalid data: " + entry);
            }
        }
    }

    private void validateUpdateData(Map<String, String> columnAndValue, Map<String, String> whereClause) throws IllegalArgumentException {
        if (columnAndValue.isEmpty() || whereClause.isEmpty()) {
            throw new IllegalArgumentException("Invalid data for update: " + columnAndValue + " " + whereClause);
        }
    }

    private HashMap<String, String> createStatusMap(String status) {
        HashMap<String, String> statusMap = new HashMap<>();
        statusMap.put(Constants.STATUS, status);
        return statusMap;
    }
}
