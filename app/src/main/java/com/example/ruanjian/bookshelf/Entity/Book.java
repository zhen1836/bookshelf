package com.example.ruanjian.bookshelf.Entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Book implements Serializable {
    private UUID id;
    private String title;
    private String author;
    private String translator;
    private String publisher;
    private String pubdate;
    private String ISBN;
    private int coverId;
    private String state;
    private List<Bookshelf> bookShelfs;
    private String notes;
    private List<Label> labels;
    private String sourceWeb;

    public Book() {
        this.id = UUID.randomUUID();
        bookShelfs = new LinkedList<>();
        labels = new LinkedList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getCoverId() {
        return coverId;
    }

    public void setCoverId(int coverId) {
        this.coverId = coverId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Bookshelf> getBookShelfs() {
        return bookShelfs;
    }

    public void setBookShelfs(List<Bookshelf> bookShelfs) {
        this.bookShelfs = bookShelfs;
    }

    public void addBookshelf(Bookshelf bookshelf) { this.bookShelfs.add(bookshelf); }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public void addLabel(Label label) { this.labels.add(label); }

    public String getSourceWeb() {
        return sourceWeb;
    }

    public void setSourceWeb(String sourceWeb) {
        this.sourceWeb = sourceWeb;
    }
}
