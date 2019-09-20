package com.masthuggis.boki.model;

import java.net.URL;
import java.util.Date;
import java.util.Iterator;

/**
 * Interface defining common functionality of Advertisements.
 */
public interface Advertisement {


    Date getDatePublished();

    String getImgURL();

    String getTitle();

    int getPrice();

    String getUniqueOwnerID();

    String getDescription();
    Iterator <String> getTags();
    Advert.Condition getConditon();
    String getUniqueID();


}