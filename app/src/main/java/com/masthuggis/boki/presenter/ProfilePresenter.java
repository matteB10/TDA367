package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.ClickDelayHelper;
import com.masthuggis.boki.utils.StylingHelper;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.List;

public class ProfilePresenter implements IProductsPresenter {
    private final View view;
    private List<Advertisement> adverts;


    public ProfilePresenter(View view) {
        this.view = view;
        this.view.showLoadingScreen();
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
        return ClickDelayHelper.canProceedWithTapAction();
    }

    private void setCondition(Advertisement a, ThumbnailView thumbnailView) {
        int drawable = StylingHelper.getConditionDrawable(a.getCondition());
        int text = StylingHelper.getConditionText(a.getCondition());
        thumbnailView.setCondition(text, drawable);
    }

    public interface View {
        void updateItemsOnSale();

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
