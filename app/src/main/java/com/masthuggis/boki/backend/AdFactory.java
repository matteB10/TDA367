package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;

import java.util.List;

/**
 * Factory class for creating Advertisements. Provides a few ways to create new Advertisements.
 */
public class AdFactory {

     static Advertisement createAd(String datePublished, String uniqueOwnerID, String id, String title, String description, long price,
                                         Advert.Condition condition,String imageUrl, List<String> tags,String owner) {

        return new Advert(datePublished, uniqueOwnerID, id, title, description, price, condition, imageUrl, tags, owner);
    }

    public static Advertisement createAd() {
        return new Advert();
    }


}
