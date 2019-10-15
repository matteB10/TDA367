package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.ClickDelayHelper;
import com.masthuggis.boki.utils.StylingHelper;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.List;

public class FavouritesPresenter implements IProductsPresenter {

    private final AdvertsPresenterView view;
    private List<Advertisement> favourites;
    private DataModel dataModel;

    public FavouritesPresenter(AdvertsPresenterView view, DataModel dataModel) {
        this.view = view;
        this.dataModel = dataModel;
        view.showLoadingScreen();
        this.favourites = this.dataModel.getUserFavourites();
        view.hideLoadingScreen();
    }


    @Override
    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
        Advertisement advert = favourites.get(position);
        thumbnailView.setId(advert.getUniqueID());
        thumbnailView.setTitle(advert.getTitle());
        thumbnailView.setPrice(advert.getPrice());
        setCondition(advert, thumbnailView);
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

    private void setCondition(Advertisement a, ThumbnailView thumbnailView) {
        int drawable = StylingHelper.getConditionDrawable(a.getCondition());
        int text = StylingHelper.getConditionText(a.getCondition());
        thumbnailView.setCondition(text, drawable);
    }
}
