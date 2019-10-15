package com.masthuggis.boki.presenter;

public interface AdvertsPresenterView {
    void showLoadingScreen();

    void updateThumbnails();

    void showNoThumbnailsAvailableScreen();

    void hideNoThumbnailsAvailableScreen();

    void showDetailsScreen(String id);

    void hideLoadingScreen();

}
