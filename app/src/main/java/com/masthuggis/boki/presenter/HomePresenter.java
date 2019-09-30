package com.masthuggis.boki.presenter;

import android.os.Handler;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.sorting.SortManager;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.ArrayList;
import java.util.List;

/**
 * HomePresenter is the presenter class for the view called HomeFragment.
 *
 */
public class HomePresenter implements IProductsPresenter {
    private final View view;
    private final SortManager sortManager;
    private List<Advertisement> adverts;

    public HomePresenter(View view) {
        this.view = view;
        this.sortManager = SortManager.getInstance();

        this.view.showLoadingScreen();

        // Uncommet if using local JSON
        getMockData();

        // If using firebase uncommment line below
        //getData();
    }

    private void getData() {
        Repository.getInstance().getAllAds(advertisements -> {
            if (advertisements != null) {
                update(advertisements);
            }
        });
    }

    private void getMockData() {
        Handler handler = new Handler();
        handler.postDelayed(() -> update(Repository.getInstance().getMockOfAllAds()), 500);
    }

    private void update(List<Advertisement> adverts) {
        this.adverts = new ArrayList<>(adverts);
        sortUsingTheStandardSortingOption();
        this.view.hideLoadingScreen();
        this.view.updateThumbnails();
    }

    private void sortUsingTheStandardSortingOption() {
        sortOptionSelected(0);
    }

    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
        if (adverts.size() < position || adverts == null)
            return;

        Advertisement a = adverts.get(position);
        thumbnailView.setId(a.getUniqueID());
        thumbnailView.setTitle(a.getTitle());
        thumbnailView.setPrice(a.getPrice());
        if (a.getImageFile() != null) {
            thumbnailView.setImageURL(a.getImageFile().toURI().toString());
        }
    }

    public int getItemCount() {
        if (adverts == null) {
            return 0;
        }
        return adverts.size();
    }

    public void onRowPressed(String uniqueIDoFAdvert) {
        view.showDetailsScreen(uniqueIDoFAdvert);
    }

    private String[] convertListToArray(List<String> list) {
        String arr[] = new String[list.size()];
        for (int i=0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public String[] getSortOptions() {
        return convertListToArray(sortManager.getSortOptions());
    }

    public void sortOptionSelected(int pos) {
        if (adverts == null)
            return;

        List<Advertisement> sortedList = sortManager.sort(pos, adverts);
        adverts = new ArrayList<>(sortedList);
        view.updateThumbnails();
    }

    public interface View {
        void showLoadingScreen();

        void updateThumbnails();

        void hideLoadingScreen();

        void showDetailsScreen(String id);
    }
}
