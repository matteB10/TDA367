package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;

public class MainPresenter {
    private View view;
    private DataModel dataModel;

    public MainPresenter(View view) {
        this.view = view;
        this.dataModel = DataModel.getInstance();

        if (!dataModel.isLoggedIn()) {
            view.showSignUpScreen();
        }
    }

    public interface View {
        void showSignUpScreen();
    }
}