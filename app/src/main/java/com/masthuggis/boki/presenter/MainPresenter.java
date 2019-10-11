package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;

public class MainPresenter {
    private View view;
    private DataModel dataModel;
    public MainPresenter(View view, DataModel dataModel) {
        this.view = view;
        this.dataModel = DataModel.getInstance();

        if (this.dataModel.isLoggedIn()) {
            // Initilize the data by fetching the newest version from database.
            this.dataModel.fetchAllAdverts(advertisements -> {});
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