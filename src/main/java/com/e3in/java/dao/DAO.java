package com.e3in.java.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface DAO {
    // Sélectionne une seule ligne de la table spécifiée en fonction des colonnes et des critères fournis
    HashMap<String, String> select(String tableName, List<String> columnNames, HashMap<String, String> whereClause);

    // Sélectionne toutes les lignes de la table spécifiée en fonction des colonnes et des critères fournis
    ArrayList<HashMap<String, String>> selectAll(String tableName, List<String> columnNames, HashMap<String, String> whereClause);

    // Insère une nouvelle ligne dans la table spécifiée avec les colonnes et les valeurs fournies
    HashMap<String, String> insert(String tableName, LinkedHashMap<String, String> columnAndValue);

    // Met à jour les lignes de la table spécifiée en fonction des colonnes, des valeurs et des critères fournis
    HashMap<String, String> update(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause);

    // Supprime les lignes de la table spécifiée en fonction des critères fournis
    HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause);
}
