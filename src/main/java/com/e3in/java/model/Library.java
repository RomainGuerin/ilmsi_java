package com.e3in.java.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;

public class Library {
    @XmlElement(name = "livre")
    private ArrayList<Book> books;

    public void addBook(Book book) {
        this.books.add(book);
    }
    public ArrayList<Book> getBooks() {
        return this.books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
}
