package com.masthuggis.boki.view;


/**
 * An abstraction of what a thumbnail view needs to know in order to render an advert. With
 * this middle layer the view will not know about the model.
 */
public interface ThumbnailView {
    void setTitle(String name);

    void setPrice(long price);

    void setImageUrl(String url);

    void setId(String id);

    void setCondition(String condition, int color);
}
