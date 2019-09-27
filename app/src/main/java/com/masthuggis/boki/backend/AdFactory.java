package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.UniqueIdCreator;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Factory class for creating Advertisements. Provides a few ways to create new Advertisements.
 */
public class AdFactory {


    /*public static Advertisement createAd(Date datePublished, String uniqueOwnerID, String uniqueAdID, String title, String description, long price, Advert.Condition condition) {


        return new Advert(datePublished, uniqueOwnerID, uniqueAdID, title, description, price, condition);
    }*/

    public static Advertisement createAd(String datePublished, String uniqueOwnerID, String id, String title, String imgURLs, String description, long price, Advert.Condition condition) {

        return new Advert(datePublished, uniqueOwnerID, id, title, imgURLs, description, price, condition);
    }

    public static Advertisement createAd() {
        String uniqueAdId = UniqueIdCreator.getUniqueID();
        return new Advert("", "Test", uniqueAdId, "", "", "", 0, Advert.Condition.UNDEFINED);
    }


}
