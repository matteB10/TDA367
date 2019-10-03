package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;

import java.io.File;
import java.util.List;

/**
 * Factory class for creating Advertisements. Provides a few ways to create new Advertisements.
 */
public class AdFactory {


    /*public static Advertisement createAd(Date datePublished, String uniqueOwnerID, String uniqueAdID, String title, String description, long price, Advert.Condition condition) {


        return new Advert(datePublished, uniqueOwnerID, uniqueAdID, title, description, price, condition);
    }*/


    public static Advertisement createAd(String datePublished, String uniqueOwnerID, String id, String title, String description, long price,
                                         Advert.Condition condition, File imageFile, String imageUrl, List<String> tags) {

        return new Advert(datePublished, uniqueOwnerID, id, title, description, price, condition, imageFile, imageUrl, tags);
    }

    public static Advertisement createAd() {
        return new Advert();
    }


}
