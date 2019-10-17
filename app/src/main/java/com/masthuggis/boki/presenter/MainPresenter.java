package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.model.DataModel;

public class MainPresenter {
    private View view;
    private DataModel dataModel;

    public MainPresenter(View view, DataModel dataModel) {
        this.view = view;
        this.dataModel = dataModel;
    }

    public void init(boolean favouriteNav) {
        if (dataModel.isLoggedIn()) {
            dataModel.initUser(() -> {
                if (favouriteNav) {
                    view.showFavouritesScreen();
                } else {
                    view.showMainScreen();
                }
            });
        } else {
            view.showSignInScreen();
        }
    }
    
    public interface View {
        void showSignInScreen();

        void showMainScreen();

        void showFavouritesScreen();
    }
}