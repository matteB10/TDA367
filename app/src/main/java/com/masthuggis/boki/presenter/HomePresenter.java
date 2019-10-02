package com.masthuggis.boki.presenter;

import android.os.Handler;


import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.sorting.SortManager;
import com.masthuggis.boki.utils.ConditionStylingHelper;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * HomePresenter is the presenter class for the view called HomeFragment.
 */
public class HomePresenter implements IProductsPresenter {
    private final View view;
    private final SortManager sortManager;
    private List<Advertisement> adverts;

    public HomePresenter(View view) {
        this.view = view;
        this.sortManager = SortManager.getInstance();

        this.view.showLoadingScreen();

        // Used when using local JSON, comment if using firebase
        //useTestData();

        // If using firebase uncommment line below
        getData();
    }

    private void getData() {
        Repository.getInstance().getAllAds(advertisements -> {
            if (advertisements != null) {
                updateData(advertisements);
            }
        });
    }

    // Used during development when using local data
    private void useTestData() {
        Handler handler = new Handler();
        handler.postDelayed(() -> updateData(Repository.getInstance().getLocalJSONAds()), 500);
    }

    private void updateData(List<Advertisement> adverts) {
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
        try {
            Thread.sleep(300); //TODO tweak number or make better implementation
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Advertisement a = adverts.get(position);
        thumbnailView.setId(a.getUniqueID());
        thumbnailView.setTitle(a.getTitle());
        thumbnailView.setPrice(a.getPrice());
        setCondition(a, thumbnailView);
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

    private void setCondition(Advertisement a, ThumbnailView thumbnailView) {
        int drawable = ConditionStylingHelper.getConditionDrawable(a.getCondition());
        int text = ConditionStylingHelper.getConditionText(a.getCondition());
        thumbnailView.setCondition(text, drawable);
    }

    private String[] convertListToArray(List<String> list) {
        String arr[] = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public List<Advertisement> getLocalAdList() {
        return this.adverts;
    }

    public String[] getSortOptions() {
        return convertListToArray(sortManager.getSortOptions());
    }

    public void sortOptionSelected(int pos) {
        if (adverts == null)
            return;

        List<Advertisement> sortedList = sortManager.sort(pos, adverts);
        if (sortedList != null) //otherwise nullPointer is produced when zero adverts are available
            adverts = new ArrayList<>(sortedList);
        view.updateThumbnails();
    }

    //Should probably run on its own thread
    public void filter(String query) {
        Repository.getInstance().getAllAds(advertisements -> {
            if (advertisements != null)
                adverts = advertisements;
            ArrayList<Advertisement> filteredList = new ArrayList<>();
            Iterator<Advertisement> iterator = adverts.iterator();
            while (iterator.hasNext()) {
                Advertisement ad = iterator.next();
                if (ad.getTitle().toLowerCase().contains(query.toLowerCase()))
                    filteredList.add(ad);
            }
            updateData(filteredList); //TODO fix this
        });

    }
    //Has to update list of adverts so it's representative of actual database when search is performed


    public void resetAdvertList() {
        getData();
    }


    public interface View {
        void showLoadingScreen();

        void updateThumbnails();

        void hideLoadingScreen();

        void showDetailsScreen(String id);
    }
}
