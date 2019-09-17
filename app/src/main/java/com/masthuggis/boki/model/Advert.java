package com.masthuggis.boki.model;

/**
 * Represents an Advert.
 * Only required fields are a User object and a date when advert was created.
 * All other information gathered from object being sold.
 */
public class Advert implements Advertisement {
    private User seller;
    private String datePublished;
    private Book bookForSale;

    public Advert(User seller, String datePublished, Book bookForSale) {
        this.seller = seller;
        this.datePublished = datePublished;
        this.bookForSale = bookForSale;
    }

     public User getSeller() {
        return this.seller;
    }

    public String getDatePublished() {
        return this.datePublished;
    }

    public Book getBookForSale() {
        return this.bookForSale;
    }


}
