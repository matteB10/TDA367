package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.backend.RepositoryObserver;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProfilePresenter implements IProductsPresenter, RepositoryObserver {
    private View view;
    private List<Advertisement> userItemsOnSale;

    public ProfilePresenter(View view) {
        this.view = view;
        // TODO: for now using temp data, later use real advertisment of the actual user
        this.userItemsOnSale = Repository.getInstance().getAllAds();
        Repository.getInstance().addObserver(this);
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
        if (a.getImgURL() != null) {
                thumbnailView.setImageUrl(a.getImageFile());
            }
        }


    @Override
    public int getItemCount() {
        return userItemsOnSale.size();
    }

    @Override
    public void onRowPressed(String uniqueIDoFAdvert) {
        // TODO
    }

    @Override
    public void userAdvertsForSaleUpdate(Iterator<Advertisement> advertsForSale) {
        updateUserItemsOnSale(advertsForSale);
        view.updateItemsOnSale();
    }

    private void updateUserItemsOnSale(Iterator<Advertisement> advertsForSale) {
        List<Advertisement> updatedAdverts = new ArrayList<>();
        while (advertsForSale.hasNext()) {
            updatedAdverts.add(advertsForSale.next());
        }
        userItemsOnSale = updatedAdverts;
    }

    public interface View {
        void setIsUserLoggedIn(boolean isUserLoggedIn);
        void updateItemsOnSale();
        void showSettingsScreen();
    }
}
