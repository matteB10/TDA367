package com.masthuggis.boki.presenter;

public interface IListPresenter<T> {
    void onBindThumbnailViewAtPosition(int position, T thumbnailView);
    int getItemCount();
    void onRowPressed(String uniqueIDoFAdvert);
}
