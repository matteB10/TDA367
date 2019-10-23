package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;

/**
 *
 * Used by MainActivity.
 * Written by masthuggis
 */
public class MainPresenter {
    private View view;
    private DataModel dataModel;

    public MainPresenter(View view, DataModel dataModel) {
        this.view = view;
        this.dataModel = dataModel;
    }

    public void init(boolean shouldNavigateToFavoriteScreen) {
        view.hideBottomNavBar();
        if (dataModel.isLoggedIn()) {
            dataModel.initUser(() -> {
                if (shouldNavigateToFavoriteScreen) {
                    view.showFavouritesScreen();
                } else {
                    view.showMainScreen();
                }
                view.showBottomNavBar();
            });
        } else {
            view.showSignInScreen();
        }
    }

    public interface View {
        void showSignInScreen();

        void showMainScreen();

        void showFavouritesScreen();

        void hideBottomNavBar();

        void showBottomNavBar();
    }
}