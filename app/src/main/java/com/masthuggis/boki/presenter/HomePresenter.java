package com.masthuggis.boki.presenter;

import android.util.Log;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.backend.advertisementCallback;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.List;

/**
 * HomePresenter is the presenter class for the view called HomeFragment.
 *
 */
public class HomePresenter implements IProductsPresenter {
    private View view;
    private List<Advertisement> adverts;

    public HomePresenter(View view) {
        this.view = view;
        view.showLoadingScreen();
        Repository.getInstance().fetchAllAdverts(advertisements -> {
            adverts = advertisements;
            view.hideLoadingScreen();
            view.showThumbnails();
        });
    }
    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
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
        // TODO: navigate to new screen with fetched mvp
        // Question: Should presenter do navigation or some navigationManager?
        Log.d("PRINT", "Row id pressed " + uniqueIDoFAdvert);
        view.showDetailsScreen(uniqueIDoFAdvert);
    }

    public interface View {
        void showLoadingScreen();

        void showThumbnails();

        void hideLoadingScreen();

        void showDetailsScreen(String id);
    }
}
