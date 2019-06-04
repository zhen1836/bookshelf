package com.example.ruanjian.bookshelf.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Label implements Serializable {
    private String title;
    private List<Book> bookList;

    public Label(String title) {
        this.title = title;
        bookList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public void add(Book book) {
        bookList.add(book);
    }
}
