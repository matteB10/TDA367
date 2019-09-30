package com.masthuggis.boki.presenter;

import android.util.Log;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.ArrayList;
import java.util.List;

/**
 * HomePresenter is the presenter class for the view called HomeFragment.
 *
 */
public class HomePresenter implements IProductsPresenter {
    private final View view;
    private List<Advertisement> adverts;

    public HomePresenter(View view) {
        this.view = view;

        this.view.showLoadingScreen();
        Repository.getInstance().getAllAds(advertisements -> {
            if (advertisements != null) {
                this.adverts = new ArrayList<>(advertisements);
                this.view.hideLoadingScreen();
                this.view.showThumbnails();
            }
        });
    }

    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
        if (this.adverts.size() < position)
            return;

        Advertisement a = adverts.get(position);
        thumbnailView.setId(a.getUniqueID());
        thumbnailView.setTitle(a.getTitle());
        thumbnailView.setPrice(a.getPrice());
        if (a.getImgURL() != null) {
            thumbnailView.setImageUrl(a.getImgURL());
        }
    }

    public int getItemCount() {
        return adverts.size();
    }

    public void onRowPressed(String uniqueIDoFAdvert) {
        view.showDetailsScreen(uniqueIDoFAdvert);
    }

    public void sortOptionSelected(int pos) {
        Log.d("DEBUG", Integer.toString(pos));
    }

    public interface View {
        void showLoadingScreen();

        void showThumbnails();

        void hideLoadingScreen();

        void showDetailsScreen(String id);
    }
}
