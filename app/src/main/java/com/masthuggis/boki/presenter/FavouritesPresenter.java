package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.ClickDelayHelper;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.List;

public class FavouritesPresenter implements IProductsPresenter {

    private final AdvertsPresenterView view; //TODO maybe have to change this
    private List<Advertisement> favourites;

    public FavouritesPresenter(AdvertsPresenterView view) {
        this.view = view;
        view.showLoadingScreen();
        this.favourites = DataModel.getInstance().getUserFavourites();
        view.hideLoadingScreen();
    }


    @Override
    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
        Advertisement advert = favourites.get(position);
        thumbnailView.setId(advert.getUniqueID());
        thumbnailView.setTitle(advert.getTitle());
        thumbnailView.setPrice(advert.getPrice());
        if (advert.getImageUrl() != null) {
            thumbnailView.setImageURL(advert.getImageUrl());
        }
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    @Override
    public void onRowPressed(String uniqueIDOfAdvert) {
        if (view == null) {
            return;
        }
        if (canProceedWithTapAction()) {
            view.showDetailsScreen(uniqueIDOfAdvert);
        }
    }

    private boolean canProceedWithTapAction() {
        return ClickDelayHelper.canProceedWithTapAction();
    }
}
