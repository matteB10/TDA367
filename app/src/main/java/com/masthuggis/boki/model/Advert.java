package com.masthuggis.boki.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an Advert.
 * Only required fields are a User object and a date when advert was created.
 * All other information gathered from object being sold.
 */
public class Advert implements Advertisement {

    public enum Condition {
        NEW, GOOD, OK, BAD,UNDEFINED
    }

    private Date datePublished;
    private String uniqueOwnerID;
    private String title;
    private List<String> imgURLs;
    private String imageURL;
    private String description;
    private int price;
    private Condition condition;


    public Advert(Date datePublished, String uniqueOwnerID, String title, List<String> imgURLs, String description, int price,Condition condition) {
        this.datePublished = datePublished;
        this.uniqueOwnerID = uniqueOwnerID;
        this.title = title;
        this.imgURLs = imgURLs;
        imgURLs.add("TEST");
        this.description = description;
        this.price = price;
        this.condition = condition;
    }
    //Alternative constructor with only one image allowed
    public Advert(Date datePublished, String uniqueOwnerID, String title, String imgURL, String description, int price,Condition condition) {
        this.datePublished = datePublished;
        this.uniqueOwnerID = uniqueOwnerID;
        this.title = title;
        this.imageURL = imgURL;
        imgURLs.add("TEST");
        this.description = description;
        this.price = price;
        this.condition = condition;
    }

    public Date getDatePublished() {
        return this.datePublished;
    }

    @Override
    public Iterator<String> getImgURLs() {
        return this.imgURLs.iterator();
    }

    @Override
    public String getTitle() {
        return this.title;
    }


    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public String getUniqueOwnerID() {
        return this.uniqueOwnerID;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Iterator<String> getTags() {
        return null;
    }

    @Override
    public Condition getConditon() {
        return this.condition;
    }


}
