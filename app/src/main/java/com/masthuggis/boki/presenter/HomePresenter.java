package com.masthuggis.boki.presenter;

public class HomePresenter {
    private View view;

    public HomePresenter(View view) {
        this.view = view;
    }

    public interface View {
        void showLoadingScreen();
        void hideLoadingScreen();
        void setProductItems();
    }
}
