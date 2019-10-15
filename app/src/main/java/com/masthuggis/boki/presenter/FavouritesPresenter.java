package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;

import com.masthuggis.boki.model.sorting.SortManager;

import java.util.List;

public class FavouritesPresenter extends AdvertsPresenter {

    private final AdvertsPresenterView view;

    public FavouritesPresenter(AdvertsPresenterView view, DataModel dataModel) {
        super(view, dataModel);
        this.view = view;
u    }

    public List<Advertisement> getData() {
        return super.dataModel.getUserFavourites();
    }

    @Override
    public List<Advertisement> sort(List<Advertisement> adverts) {
        return SortManager.getInstance().sortWithDefaultSorting(adverts);
    }
}
