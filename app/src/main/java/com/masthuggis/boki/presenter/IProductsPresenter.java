package com.masthuggis.boki.presenter;

import com.masthuggis.boki.view.ThumbnailView;

public interface IProductsPresenter {
    void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView);
    int getItemCount();
    void onRowPressed(String uniqueIDoFAdvert);
}
