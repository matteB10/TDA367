package com.masthuggis.boki.view;

import com.masthuggis.boki.model.Advert;

import java.io.File;

/**
 * An abstraction of what a thumbnail view needs to know in order to render an advert. With
 * this middle layer the view will not know about the model.
 */
public interface ThumbnailView {
    void setTitle(String name);

    void setPrice(long price);

    void setImageUrl(File url);

    void setId(String id);

    void setCondition(Advert.Condition conditon);
}
