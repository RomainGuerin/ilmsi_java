package com.e3in.java.DAO;

import com.e3in.java.utils.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BaseSQL {
    SQLiteConnection connection;

    public BaseSQL() {
        connection = SQLiteConnection.getInstance();
    }

    public List<Object> getElements(String nomTable, List<String> nomColonnes, HashMap<String, String> valeursWhere) {
        List<Object> elements = new ArrayList<>();
        Connection conn = connection.getConnection();
        try {
            //String colonnes = !nomColonnes.isEmpty() ? String.join(", ", nomColonnes) : "*";
            //String where = !valeursWhere.isEmpty() ? " WHERE " + String.join(" AND ", valeursWhere.keySet() + " = " + valeursWhere.values()) : "";
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM livre";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                rs.getRow();
                elements.add(rs.getString("titre"));
                System.out.println(rs.getString("titre"));
            }
            System.out.println(elements);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des éléments de la table " + nomTable + ".");
        } finally {
            SQLiteConnection.close();
        }
        return elements;
    }
}
