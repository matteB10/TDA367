package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;

import java.util.Date;
import java.util.List;

public class AdFactory {


   public static Advert createAd(Date datePublished, String uniqueOwnerID, String title, List<String> imgURLs, String description, int price, Advert.Condition condition) {


        return new Advert(datePublished,uniqueOwnerID,title,imgURLs,description,price,condition);
    }
    public static Advert createAd(Date datePublished, String uniqueOwnerID, String id, String title, String imgURLs, String description, int price, Advert.Condition condition) {


        return new Advert(datePublished,uniqueOwnerID,id,title,imgURLs,description,price,condition);
    }


}
