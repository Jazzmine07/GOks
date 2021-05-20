package com.mobdeve.calaranan.k_ilagan.m.goks;

import java.util.ArrayList;

public class Book {
    private String bookTitle;
    private ArrayList<String> authors;
    private String bookCover;
    private String bookDesc;
    private String bookPublisher;
    private String publishDate;

    private String previewLink;
    private String infoLink;
    private String buyLink;

    public Book(String bookCover, String bookTitle, ArrayList<String> authors, String bookDesc, String bookPublisher, String publishDate, String previewLink, String infoLink) {
        this.bookCover = bookCover;
        this.bookTitle = bookTitle;
        this.authors = authors;
        this.bookDesc = bookDesc;
        this.bookPublisher = bookPublisher;
        this.publishDate = publishDate;
        this.previewLink = previewLink;
        this.infoLink = infoLink;
        this.buyLink = buyLink;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public String getBookCover() {
        return bookCover;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public String getBuyLink() {
        return buyLink;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }

    public void setBuyLink(String buyLink) {
        this.buyLink = buyLink;
    }
}
