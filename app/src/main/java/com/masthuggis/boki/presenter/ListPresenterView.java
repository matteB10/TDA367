package com.masthuggis.boki.presenter;

/**
 *
 * Used by ChatPresenter, HomePresenter,ListPresenter, ListView,FavoritesPresenter and ProfilePresenter.
 * Written by masthuggis
 */
public interface ListPresenterView {
    void showLoadingScreen();

    void updateThumbnails();

    void showNoThumbnailsAvailableScreen();

    void hideNoThumbnailsAvailableScreen();

    void showDetailsScreen(String id);

    void hideLoadingScreen();
}
