package com.masthuggis.boki.model;

import java.util.List;

/**
 * Interface defining common functionality of Advertisements.
 */
public interface Advertisement {

    String getDatePublished();

    String getImageUrl();

    String getTitle();

    long getPrice();

    String getUniqueOwnerID();

    String getDescription();

    List<String> getTags();

    Condition getCondition();

    String getUniqueID();

    void setTitle(String title);

    void setPrice(int price);

    void setDescription(String description);

    void toggleTag(String tag);

    void setCondition(Condition condition);

    void setDatePublished(String datePublished);

    boolean isNewTag(String tag);

    boolean isNewCondition(Condition condition);

    boolean isValidCondition();
    String getOwner();
}