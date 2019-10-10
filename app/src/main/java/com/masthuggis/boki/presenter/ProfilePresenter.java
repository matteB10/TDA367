package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.callbacks.advertisementCallback;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.observers.AdvertisementObserver;
import com.masthuggis.boki.model.sorting.SortManager;

import java.util.List;

public final class ProfilePresenter<T extends AdvertsPresenterView & ProfilePresenter.View> extends AdvertsPresenter implements AdvertisementObserver {

    private final T profileView;

    public ProfilePresenter(T view) {
        super(view);
        this.profileView = view;
        super.updateData();
        DataModel.getInstance().addMarketAdvertisementObserver(this);
    }

    @Override
    public void getData(advertisementCallback advertisementCallback) {
        DataModel.getInstance().getAdsFromLoggedInUser(adverts -> advertisementCallback.onCallback(adverts));
    }

    @Override
    public List<Advertisement> sort(List<Advertisement> adverts) {
        return SortManager.getInstance().sortWithDefaultSorting(adverts);
    }

    @Override
    public void onAdvertisementsUpdated() {
        super.updateData();
    }

    public void onSignOutPressed() {
        DataModel.getInstance().signOut();
        profileView.showLoginScreen();
    }

    public interface View {
        void showLoginScreen();
    }
}
