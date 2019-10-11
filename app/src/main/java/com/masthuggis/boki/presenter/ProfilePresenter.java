package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.callbacks.advertisementCallback;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.observers.AdvertisementObserver;
import com.masthuggis.boki.model.sorting.SortManager;

import java.util.List;

/**
 * Presenter handling the profile view. It handles a view that implements both AdvertsPresterView
 * and ProfilePresenter.View interfaces. It displays the logged in users advertisments.
 * It is an observer of the market so it can update its data
 * accordingly.
 * @param <T>
 */
public final class ProfilePresenter<T extends AdvertsPresenterView & ProfilePresenter.View> extends AdvertsPresenter implements AdvertisementObserver {

    private final T profileView;
    private DataModel dataModel;

    public ProfilePresenter(T view, DataModel dataModel) {
        super(view);
        this.dataModel = dataModel;
        this.profileView = view;
    }

    public void initPresenter() {
        dataModel.addMarketAdvertisementObserver(this);
        updateAdverts();
    }

    @Override
    public void getData(advertisementCallback advertisementCallback) {
        dataModel.getAdsFromLoggedInUser(adverts -> advertisementCallback.onCallback(adverts));
    }

    @Override
    public List<Advertisement> sort(List<Advertisement> adverts) {
        return SortManager.getInstance().sortWithDefaultSorting(adverts);
    }

    /**
     * Whenever the market is updated the view is updated using the latest data.
     */
    @Override
    public void onAdvertisementsUpdated() {
        super.updateAdverts();
    }

    public void onSignOutPressed() {
        dataModel.signOut();
        profileView.showLoginScreen();
    }

    public interface View {
        void showLoginScreen();
    }
}
