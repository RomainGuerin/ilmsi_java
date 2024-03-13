package com.e3in.java.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

public class Book {
    @XmlElement(name="titre")
    private String title;

    @XmlElement(name="auteur")
    private Author author;
    @XmlElement(name="presentation")
    private String description;
    @XmlElement(name="parution")
    private String publication;
    @XmlElement(name="colonne")
    private short column;
    @XmlElement(name="rangee")
    private short row;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public short getColumn() {
        return column;
    }

    public void setColumn(short column) {
        this.column = column;
    }

    public short getRow() {
        return row;
    }

    public void setRow(short row) {
        this.row = row;
    }
}
