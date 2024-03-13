package com.e3in.java.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainModel{
    private ObservableList<String> dataList = FXCollections.observableArrayList();

    public ObservableList<String> getDataList() {
        return dataList;
    }
}