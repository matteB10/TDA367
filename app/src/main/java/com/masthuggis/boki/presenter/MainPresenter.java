package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;

public class MainPresenter {
    private View view;
    private DataModel dataModel;

    public MainPresenter(View view, DataModel dataModel) {
        this.view = view;
        this.dataModel = dataModel;

        // Initilize the data by fetching the newest version from database.
        if (dataModel.isLoggedIn()) {
            // Initilize the data by fetching the newest version from database.
            dataModel.fetchAllAdverts(advertisements -> view.showMainScreen());
        } else {
            view.showSignInScreen();
        }
    }

    public interface View {
        void showSignInScreen();

        void showMainScreen();
    }
}