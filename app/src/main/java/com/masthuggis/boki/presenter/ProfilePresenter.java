package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.observers.AdvertisementObserver;
import com.masthuggis.boki.utils.ClickDelayHelper;
import com.masthuggis.boki.utils.StylingHelper;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.List;

public class ProfilePresenter implements IProductsPresenter, AdvertisementObserver {
    private final View view;
    private List<Advertisement> adverts;

    public ProfilePresenter(View view) {
        this.view = view;
        getData();
        DataModel.getInstance().addMarketAdvertisementObserver(this);
    }

    private void getData() {
        this.view.showLoadingScreen();
        DataModel.getInstance().getAdsFromLoggedInUser(advertisements -> updateData(advertisements));
    }

    private void updateData(List<Advertisement> adverts) {
        if (adverts == null)
            return;

        this.adverts = adverts;
        view.hideLoadingScreen();
        view.updateThumbnails();
    }

    @Override
    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
        Advertisement a = adverts.get(position);
        thumbnailView.setId(a.getTitle());
        thumbnailView.setTitle(a.getTitle());
        thumbnailView.setPrice(a.getPrice());
        setCondition(a, thumbnailView);
        if (a.getImageUrl() != null) {
            thumbnailView.setImageURL(a.getImageUrl());
        }
    }

    @Override
    public int getItemCount() {
        // TODO: change to user adverts when that logic has been implemented
        // for now using same adverts as in market
        if (adverts == null) {
            return 0;
        }
        return adverts.size();
    }

    @Override
    public void onRowPressed(String uniqueIDoFAdvert) {
        // TODO
    }

    @Override
    public boolean canProceedWithTapAction() {
        return ClickDelayHelper.canProceedWithTapAction();
    }

    private void setCondition(Advertisement a, ThumbnailView thumbnailView) {
        int drawable = StylingHelper.getConditionDrawable(a.getCondition());
        int text = StylingHelper.getConditionText(a.getCondition());
        thumbnailView.setCondition(text, drawable);
    }

    @Override
    public void onAdvertisementsUpdated() {
        getData();
    }

    public interface View {
        void updateThumbnails();

        void showSettingsScreen();

        void showLoadingScreen();

        void hideLoadingScreen();

        void showLoginScreen();

    }


    //---------------------------------------
    public void onSettingsButtonPressed() {
        view.showSettingsScreen();
    }

    public void onSignOutPressed() {
        DataModel.getInstance().signOut();
        view.showLoginScreen();
    }

}
