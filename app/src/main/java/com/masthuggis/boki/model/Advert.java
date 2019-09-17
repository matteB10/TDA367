package com.masthuggis.boki.model;

/**
 * Represents an Advert.
 * Only required fields are a User object and a date when advert was created.
 * All other information gathered from object being sold.
 */
public class Advert implements Advertisement {
    private int userId;
    private String datePublished;
    private iBook bookForSale;

    public Advert(int userId, String datePublished, iBook bookForSale) {
        this.userId = userId;
        this.datePublished = datePublished;
        this.bookForSale = bookForSale;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getDatePublished() {
        return this.datePublished;
    }

    public iBook getBookForSale() {
        return this.bookForSale;
    }


}
