package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.callbacks.advertisementCallback;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;

import java.util.List;

public class MainPresenter {
    private View view;
    private DataModel dataModel;

    public MainPresenter(View view, DataModel dataModel) {
        this.view = view;
        this.dataModel = dataModel;

        if (dataModel.isLoggedIn()) {
            dataModel.initUser();
            dataModel.fetchAllAdverts(advertisements -> {
                view.showMainScreen(); //Wait with showing anything to the user until all necessary data has been loaded
            });
        } else {
            view.showSignInScreen();
        }
    }

    public interface View {
        void showSignInScreen();

        void showMainScreen();
    }
}