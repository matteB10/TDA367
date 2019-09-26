package com.masthuggis.boki.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Advert.
 * Only required fields are a User object and a date when advert was created.
 * All other information gathered from object being sold.
 */
public class Advert implements Advertisement {

    public enum Condition {
        NEW, GOOD, OK, UNDEFINED
    }

    private String datePublished;
    private String uniqueOwnerID;
    private String uniqueAdID;
    private String title;
    private List<String> imgURLs;
    private String imageURL;
    private String description;
    private long price;
    private Condition condition;
    private List<String> tags = new ArrayList<>();
    private File imageFile;


    /**public Advert(Date datePublished, String uniqueOwnerID, String uniqueAdID, String title, List<String> imgURLs, String description, long price, Condition condition) {
        this.datePublished = datePublished;
        this.uniqueOwnerID = uniqueOwnerID;
        this.title = title;
        this.imgURLs = imgURLs;
        imgURLs.add("TEST"); //Gives nullPointerException when loading all adverts???
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.uniqueAdID = uniqueAdID;
    }
     */

    //Alternative constructor with only one image allowed
    public Advert(String datePublished, String uniqueOwnerID, String id, String title, String imgURL, String description, long price, Condition condition) {
        this.datePublished = datePublished;
        this.uniqueOwnerID = uniqueOwnerID;
        this.uniqueAdID = id;
        this.title = title;
        this.imageURL = imgURL;
        this.description = description;
        this.price = price;
        this.condition = condition;
    }

    public Advert(String datePublished, String uniqueOwnerID, String id, String title, String imgURL, String description, long price, Condition condition,File file) {
        this.datePublished = datePublished;
        this.uniqueOwnerID = uniqueOwnerID;
        this.uniqueAdID = id;
        this.title = title;
        this.imageURL = imgURL;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.imageFile = file;
    }


    public String getDatePublished() {
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
    public long getPrice() {
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
    public List<String> getTags() {
        return this.tags;
    }

    @Override
    public Condition getConditon() {
        return this.condition;
    }

    @Override
    public String getUniqueID() {
        return this.uniqueAdID;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setImgURI(String URI) {
        this.imageURL = URI;
    }

    @Override
    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void tagsChanged(String tag) {
        if (isNewTag(tag)) {
            tags.add(tag);
        } else {
            tags.remove(tag);
        }
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    @Override
    public void setCondition(Condition condition) {
        this.condition = condition;
    }


    private boolean isNewTag(String tag) {
        for (String s : tags) {
            if (s.equals(tag)) {
                return false;
            }
        }
        return true;
    }


}
