package com.masthuggis.boki.view;


import com.masthuggis.boki.utils.iConditionable;

/**
 * An abstraction of what a thumbnail view needs to know in order to render an advert. With
 * this middle layer the view will not know about the model.
 */
public interface ThumbnailView extends iConditionable {
    void setTitle(String name);

    void setPrice(long price);

    void setImageURL(String url);

    void setId(String id);

}
