package com.masthuggis.boki.model;

import com.masthuggis.boki.utils.Condition;

import java.util.List;

/**
 * Represents an Advert, implements Advertisement interface.
 * Contains all fields relevant to represent an advertisement.
 * Used by AdFactory to create new objects. All other interaction
 * with this class is done through the implemented interface.
 * Written by masthuggis
 */
public class Advert implements Advertisement {

    private String datePublished;
    private String uniqueOwnerID;
    private String uniqueAdID;
    private String title;
    private String description;
    private long price;
    private Condition condition;
    private List<String> tags;
    private String imageUrl;
    private String owner;

    Advert(String datePublished, String uniqueOwnerID, String id, String title, String description, long price, Condition condition, String imageUrl, List<String> tags, String owner) {
        this.datePublished = datePublished;
        this.uniqueOwnerID = uniqueOwnerID;
        this.uniqueAdID = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.imageUrl = imageUrl;
        this.tags = tags;
        this.owner = owner;
    }

    public String getDatePublished() {
        return this.datePublished;
    }

    @Override
    public String getImageUrl() {
        return this.imageUrl;
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
    public Condition getCondition() {
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
    public void setPrice(int price) {
        if (price >= 0) {
            this.price = price;
        }
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void toggleTag(String tag) {
        if (isNewTag(tag)) {
            tags.add(tag);
        } else {
            tags.remove(tag);
        }
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }


    public boolean isNewCondition(Condition condition) {
        return this.condition != condition;

    }

    /**
     * @param condition, string given from view, representing a condition
     */
    @Override
    public void setCondition(Condition condition) {
        if(isNewCondition(condition)) {
            switch (condition) {
                case NEW:
                    this.condition = Condition.NEW;
                    break;
                case GOOD:
                    this.condition = Condition.GOOD;
                    break;
                case OK:
                    this.condition = Condition.OK;
                    break;
                }
            }
            else{
                this.condition = Condition.UNDEFINED;
        }
    }

    @Override
    public boolean isNewTag(String tag) {
        for (String s : tags) {
            if (s.equals(tag)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isValidCondition() {
        return condition != Condition.UNDEFINED;
    }

    @Override
    public String getOwner() {
        return this.owner;
    }


}
