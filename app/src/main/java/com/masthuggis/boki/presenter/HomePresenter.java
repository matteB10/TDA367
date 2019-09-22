package com.masthuggis.boki.presenter;

import android.util.Log;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.List;

public class HomePresenter {
    private View view;
    private List<Advertisement> adverts;

    public HomePresenter(View view) {
        this.view = view;
        this.adverts = Repository.getInstance().getAllAds();
    }

    public void onBindRepositoryRowViewAtPosition(int position, ThumbnailView thumbnailView) {
        Advertisement a = adverts.get(position);
        thumbnailView.setId(a.getTitle());
        thumbnailView.setTitle(a.getTitle());
        thumbnailView.setPrice(a.getPrice());
        if (a.getImgURLs().next() != null) {
            thumbnailView.setImageUrl(a.getImgURLs().next());
        }
    }

    public int getNumAdverts() {
        return Repository.getInstance().getAllAds().size();
    }

    public void onRowPressed(String uniqueIDoFAdvert) {
        // TODO: navigate to new screen with fetched mvp
        // Question: Should presenter do navigation or some navigationManager?
        Log.d("PRINT", "Row id pressed " + uniqueIDoFAdvert);
        view.showDetailsScreen(uniqueIDoFAdvert);
    }

    public interface View {
        void showLoadingScreen();
        void hideLoadingScreen();
        void showDetailsScreen(String id);
    }
}
