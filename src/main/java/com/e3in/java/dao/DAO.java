package com.e3in.java.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * L'interface DAO définit les opérations CRUD de base pour interagir avec une base de données.
 */
public interface DAO {

    /**
     * Sélectionne une seule ligne de la table spécifiée en fonction des colonnes et des critères fournis.
     *
     * @param tableName  Le nom de la table.
     * @param columnNames La liste des colonnes à sélectionner.
     * @param whereClause Les critères de sélection sous forme de map clé-valeur.
     * @return Un HashMap représentant la ligne sélectionnée, les clés étant les noms des colonnes.
     */
    HashMap<String, String> select(String tableName, List<String> columnNames, HashMap<String, String> whereClause);

    /**
     * Sélectionne toutes les lignes de la table spécifiée en fonction des colonnes et des critères fournis.
     *
     * @param tableName  Le nom de la table.
     * @param columnNames La liste des colonnes à sélectionner.
     * @param whereClause Les critères de sélection sous forme de map clé-valeur.
     * @return Un ArrayList de HashMaps représentant les lignes sélectionnées, chaque HashMap ayant pour clés les noms des colonnes.
     */
    ArrayList<HashMap<String, String>> selectAll(String tableName, List<String> columnNames, HashMap<String, String> whereClause);

    /**
     * Insère une nouvelle ligne dans la table spécifiée avec les colonnes et les valeurs fournies.
     *
     * @param tableName      Le nom de la table.
     * @param columnAndValue Une LinkedHashMap contenant les colonnes et leurs valeurs.
     * @return Un HashMap représentant le résultat de l'insertion, les clés étant les noms des colonnes insérées.
     */
    HashMap<String, String> insert(String tableName, LinkedHashMap<String, String> columnAndValue);

    /**
     * Met à jour les lignes de la table spécifiée en fonction des colonnes, des valeurs et des critères fournis.
     *
     * @param tableName      Le nom de la table.
     * @param columnAndValue Une HashMap contenant les colonnes et leurs nouvelles valeurs.
     * @param whereClause    Les critères de sélection des lignes à mettre à jour sous forme de map clé-valeur.
     * @return Un HashMap représentant le résultat de la mise à jour, les clés étant les noms des colonnes mises à jour.
     */
    HashMap<String, String> update(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause);

    /**
     * Supprime les lignes de la table spécifiée en fonction des critères fournis.
     *
     * @param tableName   Le nom de la table.
     * @param whereClause Les critères de sélection des lignes à supprimer sous forme de map clé-valeur.
     * @return Un HashMap représentant le résultat de la suppression, les clés étant les noms des colonnes supprimées.
     */
    HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause);
}
