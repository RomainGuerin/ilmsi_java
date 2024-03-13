package com.e3in.java.controller;

import com.e3in.java.model.Bibliotheque;

public class LibraryController {
    private Bibliotheque bibliotheque;
    public LibraryController (String xmlFilePath) {
        this.bibliotheque = XmlUtils.buildLibraryFromXML(xmlFilePath);
    }
}
