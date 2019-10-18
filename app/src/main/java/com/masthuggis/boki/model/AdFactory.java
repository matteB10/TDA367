package com.masthuggis.boki.model;

import java.util.List;

/**
 * Factory class for creating Advertisements. Provides a few ways to create new Advertisements.
 */
public class AdFactory {

     public static Advertisement createAd(String datePublished, String uniqueOwnerID, String id, String title, String description, long price,
                                          Condition condition, String imageUrl, List<String> tags, String owner) {

        return new Advert(datePublished, uniqueOwnerID, id, title, description, price, condition, imageUrl, tags, owner);
    }

    public static Advertisement createAd() {
        return new Advert();
    }


}
