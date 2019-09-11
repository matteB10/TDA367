package com.masthuggis.boki.model;

/**
 * Interface defining common functionality of Advertisements.
 */
public interface Advertisement {
    User getSeller();

    String getDatePublished();
}