package com.masthuggis.boki.model;

import com.masthuggis.boki.R;
import com.masthuggis.boki.backend.BackendDataHandler;
import com.masthuggis.boki.utils.UniqueIdCreator;

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
    private String description;
    private long price;
    private Condition condition;
    private List<String> tags;
    private String imageUrl;
    private String owner;


    public Advert() {
        this.datePublished = "";
        this.uniqueOwnerID = BackendDataHandler.getInstance().getUserID();
        this.uniqueAdID = UniqueIdCreator.getUniqueID();
        this.title = "";
        this.description = "";
        this.price = 0;
        this.condition = Condition.UNDEFINED;
        this.tags = new ArrayList<>();
        this.owner = DataModel.getInstance().getUserDisplayName();
    }

    public Advert(String datePublished, String uniqueOwnerID, String id, String title, String description, long price, Condition condition, String imageUrl, List<String> tags, String owner) {
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


    /**
     * @param condition, string given from view, representing a condition
     */
    @Override
    public void setCondition(int condition) {
        switch (condition) {
            case R.string.conditionNew:
                this.condition = Condition.NEW;
                break;
            case R.string.conditionGood:
                this.condition = Condition.GOOD;
                break;
            case R.string.conditionOk:
                this.condition = Condition.OK;
                break;
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
