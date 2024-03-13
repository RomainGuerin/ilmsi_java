package com.e3in.java.controller;

import com.e3in.java.model.Library;

public class LibraryController {
    private Library library;
    public LibraryController (String xmlFilePath) {
        this.library = XmlUtils.buildLibraryFromXML(xmlFilePath);
    }
}
