package com.masthuggis.boki.view;


/**
 * An abstraction of what a thumbnail view needs to know in order to render an advert.
 * With this middle layer the view will not be aware of the existence of the model.
 * User by AdvertsPresenterHelper, FavoritesPresenter, HomePresenter,
 * ProductsRecyclerViewAdapter and ProfilePresenter.
 * Written by masthuggis.
 */
public interface ThumbnailView {
    void setTitle(String name);

    void setPrice(long price);

    void setImageURL(String url);

    void setId(String id);

    void setCondition(int color, int condition);

}
