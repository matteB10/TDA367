package com.masthuggis.boki.model;

import java.util.List;

/**
 * Interface defining common functionality of Advertisements.
 */
public interface Advertisement {

    boolean isMarkedAsFavourite();

    void markAsFavourite();

    void markAsNotFavourite();

    String getDatePublished();

    String getImageUrl();

    String getTitle();

    long getPrice();

    String getUniqueOwnerID();

    String getDescription();

    List<String> getTags();

    Advert.Condition getCondition();

    String getUniqueID();

    void setTitle(String title);

    void setPrice(int price);

    void setDescription(String description);

    void tagsChanged(String tag);

    void setCondition(int condition);

    void setDatePublished(String datePublished);

    boolean isNewTag(String tag);

    boolean isValidCondition();
    String getOwner();
}