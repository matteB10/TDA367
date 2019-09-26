package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.backend.RepositoryObserver;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.Iterator;

public class ProfilePresenter implements IProductsPresenter, RepositoryObserver {
    private final View view;
    private Repository repository;

    public ProfilePresenter(View view) {
        this.view = view;
        this.repository = Repository.getInstance();

        repository.addObserver(this);
        this.view.showLoadingScreen();
    }

    public void onSettingsButtonPressed() {
        view.showSettingsScreen();
    }

    @Override
    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
        Advertisement a = repository.getAllAds().get(position);
        thumbnailView.setId(a.getTitle());
        thumbnailView.setTitle(a.getTitle());
        thumbnailView.setPrice(a.getPrice());
        if (a.getImgURL() != null) {
            thumbnailView.setImageUrl(a.getImgURL());
        }
    }


    @Override
    public int getItemCount() {
        // TODO: change to user adverts when that logic has been implemented
        // for now using same adverts as in market
        return repository.getAllAds().size();
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
