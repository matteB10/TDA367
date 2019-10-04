package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.RepositoryObserver;
import com.masthuggis.boki.backend.UserRepository;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.StylingHelper;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.List;

public class ProfilePresenter implements IProductsPresenter {
    private final View view;
    private List<Advertisement> adverts;
    private long lastTimeThumbnailWasClicked = System.currentTimeMillis();
    private static final long MIN_CLICK_TIME_INTERVAL = 300;

    private UserRepository userRepo;

    public ProfilePresenter(View view) {
        this.view = view;
        this.view.showLoadingScreen();
        if(DataModel.getInstance().isLoggedIn()){
            this.adverts = DataModel.getInstance().getAdsFromUniqueOwnerID(DataModel.getInstance().getUserID());
        }
        this.view.hideLoadingScreen();

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
        long now = System.currentTimeMillis();
        boolean canProceed;
        if (now - lastTimeThumbnailWasClicked < MIN_CLICK_TIME_INTERVAL) {
            canProceed = false;
        } else {
            canProceed = true;
        }
        lastTimeThumbnailWasClicked = now;
        return canProceed;
    }

    private void setCondition(Advertisement a, ThumbnailView thumbnailView) {
        int drawable = StylingHelper.getConditionDrawable(a.getCondition());
        int text = StylingHelper.getConditionText(a.getCondition());
        thumbnailView.setCondition(text, drawable);
    }

    public void isLoggedIn() {
        if (DataModel.getInstance().isLoggedIn()) {
            view.setIsUserLoggedIn(true);
        } else {
            view.setIsUserLoggedIn(false);
        }

    }

    public interface View {
        void setIsUserLoggedIn(boolean isUserLoggedIn);

        void updateItemsOnSale();

        void showSettingsScreen();

        void showLoadingScreen();

        void hideLoadingScreen();

        void showSignInScreen();

    }


    //---------------------------------------
    public void onSettingsButtonPressed() {
        view.showSettingsScreen();
    }

    public void onSignInButtonPressed() {
        view.showSignInScreen();
    }

    public boolean setUpUser(){
        return userRepo.isSignedIn();
    }
}
