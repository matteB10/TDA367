package com.masthuggis.boki.model;

/**
 * Interface defining common functionality of Advertisements.
 */
public interface Advertisement {
    int getUserId();

    iBook getBookForSale();

    String getDatePublished();
}