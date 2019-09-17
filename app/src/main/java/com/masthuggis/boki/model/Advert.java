package com.masthuggis.boki.model;

/**
 * Represents an Advert.
 * Only required fields are a User object and a date when advert was created.
 * All other information gathered from object being sold.
 */
public class Advert implements Advertisement {
    private int userID;
    private String datePublished;
    private Book bookForSale;

    public Advert(int userID, String datePublished, Book bookForSale) {
        this.userID = userID;
        this.datePublished = datePublished;
        this.bookForSale = bookForSale;
    }

     public int getUserID() {
        return this.userID;
    }

    public String getDatePublished() {
        return this.datePublished;
    }

    public Book getBookForSale() {
        return this.bookForSale;
    }


}
