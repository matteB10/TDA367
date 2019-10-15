package com.masthuggis.boki.presenter;

public interface AdvertsPresenterView {
    void showLoadingScreen();

    void updateThumbnails();

    void showNoThumbnailsAvailableScreen();

    void hideLoadingScreen();

    void showDetailsScreen(String id);
}
