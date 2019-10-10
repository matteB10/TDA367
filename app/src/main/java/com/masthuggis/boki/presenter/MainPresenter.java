package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;

public class MainPresenter {
    private View view;
    private DataModel dataModel;

    public MainPresenter(View view) {
        this.view = view;
        this.dataModel = DataModel.getInstance();

        // Initilize the data by fetching the newest version from database.
        dataModel.fetchAllAdverts(advertisements -> {});

        if (dataModel.isLoggedIn()) {
            // Initilize the data by fetching the newest version from database.
            dataModel.fetchAllAdverts(advertisements -> {});
            view.showMainScreen();
        } else {
            view.showSignUpScreen();
        }
    }

    public interface View {
        void showSignUpScreen();
        void showMainScreen();
    }
}