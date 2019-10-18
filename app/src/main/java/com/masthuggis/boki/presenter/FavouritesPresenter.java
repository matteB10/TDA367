package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;

import com.masthuggis.boki.model.sorting.SortManager;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.List;

public class FavouritesPresenter extends AdvertsPresenter<Advertisement, ThumbnailView> {
    public FavouritesPresenter(ListPresenterView view, DataModel dataModel) {
        super(view, dataModel);
        this.view = view;
    }

    public List<Advertisement> getData() {
        return super.dataModel.getUserFavourites(); //User held by datamodel here is a different object
    }

    @Override
    public List<Advertisement> sort(List<Advertisement> adverts) {
        return SortManager.getInstance().sortWithDefaultSorting(adverts);
    }

    @Override
    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
        AdvertsPresenterHelper.onBindThumbnailViewAtPosition(position, thumbnailView, getCurrentDisplayedAds());
    }
}
