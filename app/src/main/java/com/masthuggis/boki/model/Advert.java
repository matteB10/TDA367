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
        NEW, GOOD, OK,UNDEFINED
    }

    private Date datePublished;
    private String uniqueOwnerID;
    private String uniqueAdID;
    private String title;
    private List<String> imgURLs;
    private String imageURL;
    private String description;
    private int price;
    private Condition condition;


    public Advert(Date datePublished, String uniqueOwnerID,String uniqueAdID, String title, List<String> imgURLs, String description, int price,Condition condition) {
        this.datePublished = datePublished;
        this.uniqueOwnerID = uniqueOwnerID;
        this.title = title;
        this.imgURLs = imgURLs;
        imgURLs.add("TEST");
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.uniqueAdID = uniqueAdID;
    }
    //Alternative constructor with only one image allowed
    public Advert(Date datePublished, String uniqueOwnerID, String id, String title, String imgURL, String description, int price,Condition condition) {
        this.datePublished = datePublished;
        this.uniqueOwnerID = uniqueOwnerID;
        this.uniqueAdID = id;
        this.title = title;
        this.imageURL = imgURL;
        this.description = description;
        this.price = price;
        this.condition = condition;
    }

    public Date getDatePublished() {
        return this.datePublished;
    }

    @Override
    public String getImgURL() {
        return this.imageURL;
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

    @Override
    public String getUniqueID() {
        return this.uniqueAdID;
    }


}
