package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.List;

public class ProfilePresenter implements IProductsPresenter {
    private View view;
    private List<Advertisement> userItemsOnSale;
    private Repository repository;

    public ProfilePresenter(View view) {
        this.view = view;
        this.repository = Repository.getInstance();
        // TODO: for now using temp data, later use real advertisment of the actual user
        this.userItemsOnSale = repository.getTemporaryListOfAllAds();
    }

    public void onSettingsButtonPressed() {
        view.showSettingsScreen();
    }

    @Override
    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
        Advertisement a = userItemsOnSale.get(position);
        thumbnailView.setId(a.getTitle());
        thumbnailView.setTitle(a.getTitle());
        thumbnailView.setPrice(a.getPrice());
        if (a.getImgURLs().next() != null) {
            thumbnailView.setImageUrl(a.getImgURLs().next());
        }
    }

    @Override
    public int getItemCount() {
        return userItemsOnSale.size();
    }

    @Override
    public void onRowPressed(String uniqueIDoFAdvert) {

    }

    public interface View {
        void setIsUserLoggedIn(boolean isUserLoggedIn);
        void updateItemsOnSale(ThumbnailView items);
        void showSettingsScreen();
    }
}
