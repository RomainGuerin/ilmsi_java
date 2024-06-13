package com.e3in.java.dao;

import java.util.HashMap;
import java.util.List;

public interface DAO {
    HashMap<String, String> select(String tableName, List<String> columnNames, HashMap<String, String> whereClause);
    HashMap<String, String> insert(String tableName, HashMap<String, String> columnAndValue);
    HashMap<String, String> update(String tableName, HashMap<String, String> columnAndValue, HashMap<String, String> whereClause);
    HashMap<String, String> delete(String tableName, HashMap<String, String> whereClause);
}
