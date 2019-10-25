package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;

import com.masthuggis.boki.utils.sorting.SortManager;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.List;

/**
 *
 * Used by FavoritesFragment
 * Written by masthuggis
 */
public class FavoritesPresenter extends ListPresenter<Advertisement, ThumbnailView> {
    public FavoritesPresenter(ListPresenterView view, DataModel dataModel) {
        super(view, dataModel);
        this.view = view;
    }

    /**
     *
     * @return
     */
    public List<Advertisement> getData() {
        removeDeletedFavourites(dataModel.getUserFavourites());
        return dataModel.getUserFavourites();
        /*List<Advertisement> userFavourites = dataModel.getUserFavourites();
        for (Advertisement advertisement : userFavourites) {
            if (!dataModel.adStillExists(advertisement.getUniqueID())) {
                dataModel.removeExistingAdvert(advertisement.getUniqueID(),advertisement.getUniqueOwnerID());
            }
        }
        return super.dataModel.getUserFavourites(); //User held by datamodel here is a different object*/
    }

    private void removeDeletedFavourites(List<Advertisement> uncheckedFavourites) {
        for (Advertisement advertisement : uncheckedFavourites) {
            if (!dataModel.adStillExists(advertisement.getUniqueID())) {
                dataModel.removeFromFavourites(advertisement);
            }
        }
    }

    @Override
    public List<Advertisement> sort(List<Advertisement> data) {
        return SortManager.getInstance().sortWithDefaultSorting(data);
    }

    @Override
    public void onBindThumbnailViewAtPosition(int position, ThumbnailView dataView) {
        AdvertsPresenterHelper.onBindThumbnailViewAtPosition(position, dataView, getCurrentDisplayedData());
    }
}
