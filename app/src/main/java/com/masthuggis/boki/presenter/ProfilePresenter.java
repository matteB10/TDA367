package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.sorting.SortManager;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.List;

/**
 * Presenter handling the profile view. It handles a view that implements both AdvertsPresterView
 * and ProfilePresenter.View interfaces. It displays the logged in users advertisments.
 * It is an observer of the market so it can update its data
 * accordingly.
 * @param <T>
 */
public final class ProfilePresenter<T extends ListPresenterView & ProfilePresenter.View> extends ListPresenter<Advertisement, ThumbnailView> {

    private final T profileView;

    public ProfilePresenter(T view, DataModel dataModel) {
        super(view, dataModel);
        this.profileView = view;
    }

    @Override
    public List<Advertisement> getData() {
        return dataModel.getAdsFromCurrentUser();
    }

    @Override
    public List<Advertisement> sort(List<Advertisement> data) {
        return SortManager.getInstance().sortWithDefaultSorting(data);
    }

    public void onSignOutPressed() {
        super.dataModel.signOut();
        profileView.showLoginScreen();
    }

    public interface View {
        void showLoginScreen();
    }

    @Override
    public void onBindThumbnailViewAtPosition(int position, ThumbnailView dataView) {
        AdvertsPresenterHelper.onBindThumbnailViewAtPosition(position, dataView, getCurrentDisplayedData());
    }
}
