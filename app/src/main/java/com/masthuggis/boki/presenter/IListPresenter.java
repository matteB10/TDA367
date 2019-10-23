package com.masthuggis.boki.presenter;

/**
 *
 * Used by ListPresenter and ProductsRecyclerViewAdapter
 * Written by masthuggis
 * @param <T>
 */
public interface IListPresenter<T> {
    void onBindThumbnailViewAtPosition(int position, T thumbnailView);
    int getItemCount();
    void onRowPressed(String uniqueIDoFAdvert);
}
