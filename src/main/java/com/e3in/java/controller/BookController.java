package com.e3in.java.controller;

import com.e3in.java.model.Author;
import com.e3in.java.model.Book;

public class BookController {
    private Book book;
    public String getTitle() {
        return this.book.getTitle();
    }

    public void setTitle(String title) {
        this.book.setTitle(title);
    }

    public Author getAuthor() {
        return this.getAuthor();
    }

    public void setAuthor(Author author) {
        this.book.setAuthor(author);
    }

    public String getDescription() {
        return this.book.getDescription();
    }

    public void setDescription(String description) {
        this.book.setDescription(description);
    }

    public String getPublication() {
        return this.book.getPublication();
    }

    public void setPublication(String publication) {
        this.book.setPublication(publication);
    }

    public short getColumn() {
        return this.book.getColumn();
    }

    public void setColumn(short column) {
        this.book.setColumn(column);
    }

    public short getRow() {
        return this.book.getRow();
    }

    public void setRow(short row) {
        this.book.setRow(row);
    }
}
