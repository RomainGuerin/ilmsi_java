package com.e3in.java.controller;

import com.e3in.java.model.Bibliotheque;
import com.e3in.java.utils.Xml;

public class LibraryController {
    private Bibliotheque bibliotheque;
    public LibraryController (String xmlFilePath) {
        this.bibliotheque = Xml.buildLibraryFromXML(xmlFilePath);
    }
}
