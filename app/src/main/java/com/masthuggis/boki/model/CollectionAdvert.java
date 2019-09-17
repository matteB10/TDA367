package com.masthuggis.boki.model;

import java.util.List;

/**
 * Represents an Advert that is itself a collection of Advert-objects.
 * Has additional fields for a Collection collectionName and collectionPrice.
 */

public class CollectionAdvert implements Advertisement {
    private final List<Advert> adverts;
    private String collectionName;
    private User seller;
    private String datePublished;
    private int collectionPrice;

    /**
     * Constructor for creating an advert for a Collection of books, each represented by their
     * corresponding Advert object.
     *
     * @param adverts         Child-adverts for the books in the collection.
     * @param seller          User which is selling the collection.
     * @param collectionName  Name of the collection, searchable.
     * @param collectionPrice Total price of the collection, set freely by user, not required.
     * @param datePublished   Date the CollectionAdvert was created.
     * @throws IllegalArgumentException if price is negative.
     */
    public CollectionAdvert(List<Advert> adverts, User seller, String collectionName, int collectionPrice, String datePublished) {
        if (collectionPrice < 0)
            throw new IllegalArgumentException("Collection Price cannot be negative!");
        this.adverts = adverts;
        this.seller = seller;
        this.collectionName = collectionName;
        this.collectionPrice = collectionPrice;
        this.datePublished = datePublished;
    }


    public String getDatePublished() {
        return this.datePublished;
    }

    public int getUserID() {
        return this.seller;
    }

    public List<Advert> getAdverts() {
        return adverts;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public int getCollectionPrice() {
        return collectionPrice;
    }
}
