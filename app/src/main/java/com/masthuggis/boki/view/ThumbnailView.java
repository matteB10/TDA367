package com.masthuggis.boki.view;
import com.masthuggis.boki.model.Advert;

public interface ThumbnailView {
    void setTitle(String name);
    void setPrice(long price);
    void setImageUrl(String url);
    void setId(String id);
    void setCondition(Advert.Condition conditon);
}
