package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;

import java.util.List;

public class FavouritesPresenter {

    public FavouritesPresenter() {

    }

    public void test() {
        List<Advertisement> favourites = DataModel.getInstance().getUserFavourites();
    }
}
