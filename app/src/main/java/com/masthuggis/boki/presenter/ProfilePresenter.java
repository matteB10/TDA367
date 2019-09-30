package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.backend.RepositoryObserver;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProfilePresenter implements IProductsPresenter, RepositoryObserver {
    private final View view;
    private List<Advertisement> adverts;

    public ProfilePresenter(View view) {
        this.view = view;

        this.view.showLoadingScreen();
        Repository.getInstance().getAllAds(advertisements -> {
            this.adverts = new ArrayList<>(advertisements);
            this.view.hideLoadingScreen();
            this.view.updateItemsOnSale();
        });
    }

    public void onSettingsButtonPressed() {
        view.showSettingsScreen();
    }

    @Override
    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
        Advertisement a = adverts.get(position);
        thumbnailView.setId(a.getTitle());
        thumbnailView.setTitle(a.getTitle());
        thumbnailView.setPrice(a.getPrice());
        if (a.getImageFile() != null) {
            thumbnailView.setImageURL(a.getImageFile().toURI().toString());
        }
    }


    @Override
    public int getItemCount() {
        // TODO: change to user adverts when that logic has been implemented
        // for now using same adverts as in market
        return adverts.size();
    }

    @Override
    public void onRowPressed(String uniqueIDoFAdvert) {
        // TODO
    }

    @Override
    public void userAdvertsForSaleUpdate(Iterator<Advertisement> advertsForSale) {
        view.hideLoadingScreen();
        view.updateItemsOnSale();
    }

    @Override
    public void allAdvertsInMarketUpdate(Iterator<Advertisement> advertsInMarket) {
        // TODO: remove this when interface is segregated
    }

    public interface View {
        void setIsUserLoggedIn(boolean isUserLoggedIn);
        void updateItemsOnSale();
        void showSettingsScreen();
        void showLoadingScreen();
        void hideLoadingScreen();
    }
}
