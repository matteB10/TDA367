package com.masthuggis.boki.model;

import java.net.URL;

/**
 * Interface defining common functionality of Advertisements.
 */
public interface Advertisement {
    int getUserId();

    iBook getBookForSale();

    String getDatePublished();
    URL getImgURL();
    String getName();
    int getPrice();
    String getUUID();
}