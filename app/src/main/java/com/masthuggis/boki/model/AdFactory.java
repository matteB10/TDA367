package com.masthuggis.boki.model;

import com.masthuggis.boki.utils.Condition;

import java.util.List;

/**
 *
 * Factory class for creating Advertisements.
 * Used by CreateAdPresenter and AdvertRepository.
 * Written by masthuggis
 */
public class AdFactory {

     public static Advertisement createAd(String datePublished, String uniqueOwnerID, String id, String title, String description, long price,
                                          Condition condition, String imageUrl, List<String> tags, String owner) {

        return new Advert(datePublished, uniqueOwnerID, id, title, description, price, condition, imageUrl, tags, owner);
    }
}
