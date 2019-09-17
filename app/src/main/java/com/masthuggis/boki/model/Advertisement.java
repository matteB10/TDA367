package com.masthuggis.boki.model;

import java.net.URL;

/**
 * Interface defining common functionality of Advertisements.
 */
public interface Advertisement {
<<<<<<< HEAD
    User getSeller();
=======
    int getUserId();
    iBook getBookForSale();
>>>>>>> 5d2d8853837136a911ae4a4d259569de259f1d7e
    String getDatePublished();
    URL getImgURL();
    String getName();
    int getPrice();
    String getUUID();
}