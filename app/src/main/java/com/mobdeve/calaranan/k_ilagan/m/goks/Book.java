package com.mobdeve.calaranan.k_ilagan.m.goks;

import java.util.ArrayList;

public class Book {
    private String bookID;
    private String bookTitle;
    private ArrayList<String> authors;
    private String bookCover;
    private String bookDesc;
    private String bookPublisher;
    private String publishDate;

    private String previewLink;
    private String pdfLink;
    private String buyLink;

    public Book(String bookID, String bookCover, String bookTitle, ArrayList<String> authors, String bookDesc, String bookPublisher, String publishDate, String previewLink) {
        this.bookID = bookID;
        this.bookCover = bookCover;
        this.bookTitle = bookTitle;
        this.authors = authors;
        this.bookDesc = bookDesc;
        this.bookPublisher = bookPublisher;
        this.publishDate = publishDate;
        this.previewLink = previewLink;
    }

    public Book (String bookID, String bookCover, String bookTitle, ArrayList<String> authors, String bookPublisher, String publishDate){
        this.bookID = bookID;
        this.bookCover = bookCover;
        this.bookTitle = bookTitle;
        this.authors = authors;
        this.bookPublisher = bookPublisher;
        this.publishDate = publishDate;
    }

    public Book (String bookID, String bookCover, String bookTitle, ArrayList<String> authors, String bookDesc, String bookPublisher, String publishDate, String previewLink, String pdfLink){
        this.bookID = bookID;
        this.bookCover = bookCover;
        this.bookTitle = bookTitle;
        this.authors = authors;
        this.bookDesc = bookDesc;
        this.bookPublisher = bookPublisher;
        this.publishDate = publishDate;
        this.previewLink = previewLink;
        this.pdfLink = pdfLink;
    }

    public Book (String bookID, String bookCover, String bookTitle, ArrayList<String> authors, String bookDesc, String bookPublisher, String publishDate, String previewLink, String pdfLink, String buyLink){
        this.bookID = bookID;
        this.bookCover = bookCover;
        this.bookTitle = bookTitle;
        this.authors = authors;
        this.bookDesc = bookDesc;
        this.bookPublisher = bookPublisher;
        this.publishDate = publishDate;
        this.previewLink = previewLink;
        this.pdfLink = pdfLink;
        this.buyLink = buyLink;
    }

    public Book(String bookID, String bookCover, String bookTitle, ArrayList<String> authors, String bookDesc, String bookPublisher, String publishDate, String previewLink, String buyLink, Boolean onSale) {
        this.bookID = bookID;
        this.bookCover = bookCover;
        this.bookTitle = bookTitle;
        this.authors = authors;
        this.bookDesc = bookDesc;
        this.bookPublisher = bookPublisher;
        this.publishDate = publishDate;
        this.previewLink = previewLink;
        this.buyLink = buyLink;
    }

    public String getBookID() {
        return bookID;
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

    public String getPdfLink(){
        return pdfLink;
    }

    public String getBuyLink(){
        return buyLink;
    }
}
