package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.callbacks.advertisementCallback;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.observers.AdvertisementObserver;
import com.masthuggis.boki.model.sorting.SortManager;

import java.util.List;

public final class ProfilePresenter<T extends AdvertsPresenterView & ProfilePresenter.View> extends AdvertsPresenter implements AdvertisementObserver {

    private final T profileView;
    private DataModel dataModel;

    public ProfilePresenter(T view, DataModel dataModel) {
        super(view);
        this.dataModel = dataModel;
        this.profileView = view;
        super.updateData();
        this.dataModel.addMarketAdvertisementObserver(this);
    }

    @Override
    public void getData(advertisementCallback advertisementCallback) {
        dataModel.getAdsFromLoggedInUser(adverts -> advertisementCallback.onCallback(adverts));
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
        dataModel.signOut();
        profileView.showLoginScreen();
    }

    public interface View {
        void showLoginScreen();
    }
}
