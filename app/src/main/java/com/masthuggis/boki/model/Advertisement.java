package com.masthuggis.boki.model;

import java.io.File;
import java.util.List;

/**
 * Interface defining common functionality of Advertisements.
 */
public interface Advertisement {


    String getDatePublished();

    String getImgURL();

    String getTitle();

    long getPrice();

    String getUniqueOwnerID();

    String getDescription();

    List<String> getTags();

    Advert.Condition getConditon();

    String getUniqueID();

    void setTitle(String title);

    void setImgURI(String URI);

    void setPrice(int price);

    void setDescription(String description);

    void tagsChanged(String tag);

    void setCondition(Advert.Condition condition);

    void setDatePublished(String datePublished);
    void setImageFile( File file);


    File getImageFile();
}