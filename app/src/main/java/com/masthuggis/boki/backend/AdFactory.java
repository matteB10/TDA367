package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.UniqueIdCreator;

import java.io.File;

/**
 * Factory class for creating Advertisements. Provides a few ways to create new Advertisements.
 */
public class AdFactory {


    /*public static Advertisement createAd(Date datePublished, String uniqueOwnerID, String uniqueAdID, String title, String description, long price, Advert.Condition condition) {


        return new Advert(datePublished, uniqueOwnerID, uniqueAdID, title, description, price, condition);
    }*/


    public static Advertisement createAd(String datePublished, String uniqueOwnerID, String id, String title, String description, long price, Advert.Condition condition, File imageFile) {

        return new Advert(datePublished, uniqueOwnerID, id, title, description, price, condition, imageFile);
    }

    public static Advertisement createAd() {
        String uniqueAdId = UniqueIdCreator.getUniqueID();
        return new Advert(uniqueAdId);
    }


}
