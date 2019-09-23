package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.UniqueIdCreator;

import java.util.Date;
import java.util.List;

public class AdFactory {


    public static Advertisement createAd(Date datePublished, String uniqueOwnerID, String uniqueAdID, String title, List<String> imgURLs, String description, int price, Advert.Condition condition) {


        return new Advert(datePublished, uniqueOwnerID, uniqueAdID, title, imgURLs, description, price, condition);
    }

    public static Advertisement createAd(Date datePublished, String uniqueOwnerID, String id, String title, String imgURLs, String description, int price, Advert.Condition condition) {

        return new Advert(datePublished, uniqueOwnerID, id, title, imgURLs, description, price, condition);
    }

    public static Advertisement createAd(){
        String uniqueAdId = UniqueIdCreator.getUniqueID();
        return new Advert(new Date(),"",uniqueAdId,"","","",0,Advert.Condition.UNDEFINED);
    }


}
