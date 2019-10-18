package com.masthuggis.boki.presenter;

public interface IProductsPresenter<T> {
    void onBindThumbnailViewAtPosition(int position, T thumbnailView);
    int getItemCount();
    void onRowPressed(String uniqueIDoFAdvert);
}
