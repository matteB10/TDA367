package com.masthuggis.boki.presenter;

/**
 * View interface implemented by ListView, handles method to display content in recyclerViews
 * Used by ChatPresenter, HomePresenter, ListPresenter, FavoritesPresenter and ProfilePresenter.
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
