package com.masthuggis.boki.presenter;

import android.util.Log;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.backend.RepositoryObserver;
import com.masthuggis.boki.backend.advertisementCallback;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.Iterator;
import java.util.List;

/**
 * HomePresenter is the presenter class for the view called HomeFragment.
 *
 */
public class HomePresenter implements IProductsPresenter, RepositoryObserver {
    private View view;
    private Repository repository;

    public HomePresenter(View view) {
        this.view = view;
        this.repository = Repository.getInstance();

        repository.addObserver(this);
        this.view.showLoadingScreen();
    }

    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
        if (repository.getAllAds().size() < position)
            return;

        Advertisement a = repository.getAllAds().get(position);
        thumbnailView.setId(a.getUniqueID());
        thumbnailView.setTitle(a.getTitle());
        thumbnailView.setPrice(a.getPrice());
        if (a.getImageFile() != null) {
            thumbnailView.setImageURL(a.getImageFile().toURI().toString());
        }
    }

    public int getItemCount() {
        return repository.getAllAds().size();
    }

    public void onRowPressed(String uniqueIDoFAdvert) {
        view.showDetailsScreen(uniqueIDoFAdvert);
    }

    @Override
    public void userAdvertsForSaleUpdate(Iterator<Advertisement> advertsForSale) {
        // TODO: break up RepositoryObserver into two interfaces so we dont have unused functions
    }

    @Override
    public void allAdvertsInMarketUpdate(Iterator<Advertisement> advertsInMarket) {
        view.hideLoadingScreen();
        view.showThumbnails();
    }

    public interface View {
        void showLoadingScreen();

        void showThumbnails();

        void hideLoadingScreen();

        void showDetailsScreen(String id);
    }
}
