package com.masthuggis.boki.model;

import java.io.File;
import java.util.List;

/**
 * Interface defining common functionality of Advertisements.
 */
public interface Advertisement {


    String getDatePublished();

    File getImageFile();

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

    void setImageFile(File imageFile);

    boolean isNewTag(String tag);

    boolean isValidCondition();
}