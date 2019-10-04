package com.masthuggis.boki.presenter;

import android.os.Handler;
import android.util.Log;


import com.bumptech.glide.Glide;
import com.masthuggis.boki.backend.MockRepository;
import com.masthuggis.boki.backend.RepositoryObserver;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.sorting.SortManager;
import com.masthuggis.boki.utils.StylingHelper;
import com.masthuggis.boki.view.FilterCallback;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * HomePresenter is the presenter class for the view called HomeFragment.
 */
public class HomePresenter implements IProductsPresenter, RepositoryObserver {
    private final View view;
    private final SortManager sortManager;
    private List<Advertisement> adverts;
    private long lastTimeThumbnailWasClicked = System.currentTimeMillis();
    private static final long MIN_THUMBNAIL_CLICK_TIME_INTERVAL = 300;

    public HomePresenter(View view) {
        this.view = view;
        this.sortManager = SortManager.getInstance();

        this.view.showLoadingScreen();

        // Used when using local JSON, comment if using firebase
        //useTestData();

        // If using firebase uncommment line below
        getData();
        DataModel.getInstance().addRepositoryObserver(this);
    }

    private void getData() {
        DataModel.getInstance().fetchAllAdverts((advertisements -> {
            if (advertisements != null) {
                updateData(advertisements);
            }
        }));
    }

    // Used during development when using local data
    private void useTestData() {
        Handler handler = new Handler();
        handler.postDelayed(() -> updateData(MockRepository.getInstance().getLocalJSONAds()), 500);
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

    @Override
    public boolean canProceedWithTapAction() {
        boolean canProceed = tapActionWasNotTooFast();
        lastTimeThumbnailWasClicked = System.currentTimeMillis();
        return canProceed;
    }

    private boolean tapActionWasNotTooFast() {
        long elapsedTimeSinceLastClick = System.currentTimeMillis() - lastTimeThumbnailWasClicked;
        return elapsedTimeSinceLastClick > MIN_THUMBNAIL_CLICK_TIME_INTERVAL;
    }

    private void setCondition(Advertisement a, ThumbnailView thumbnailView) {
        int drawable = StylingHelper.getConditionDrawable(a.getCondition());
        int text = StylingHelper.getConditionText(a.getCondition());
        thumbnailView.setCondition(text, drawable);
    }

    private String[] convertListToArray(List<String> list) {
        String arr[] = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public String[] getSortOptions() {
        return convertListToArray(sortManager.getSortOptions());
    }

    public void sortOptionSelected(int pos) {
        if (adverts == null || adverts.size() == 0) {
            return;
        }

        List<Advertisement> sortedList = sortManager.sort(pos, adverts);
        if (sortedList == null)
            return;

        adverts = new ArrayList<>(sortedList);
        view.updateThumbnails();
    }

    //Should probably run on its own thread
    //Filters the advertisements shown to the user by if their title matches the given query
    public void filter(String query, FilterCallback callback) { //TODO use callback

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DataModel.getInstance().fetchAllAdverts(advertisements -> {
                    if (advertisements != null)
                        adverts = advertisements; //Refreshes the list so it accurately reflects adverts in firebase

                    ArrayList<Advertisement> filteredList = new ArrayList<>();
                    Iterator<Advertisement> iterator = adverts.iterator();
                    while (iterator.hasNext()) {
                        Advertisement ad = iterator.next();
                        if (ad.getTitle().toLowerCase().contains(query.toLowerCase().trim())) //Only search capability on tile of advert
                            filteredList.add(ad);
                    }
                    updateData(filteredList); //TODO fix this so loading Screen shows up
                    callback.onCallback();
                });
            }
        });
        thread.start();

    }

    @Override
    public void advertsInMarketUpdate(List<Advertisement> advertsInMarket) {
        Log.d("DEBUG", "advertsInMarketUpdate " + advertsInMarket.size());
        if (advertsInMarket != null && !advertsInMarket.isEmpty())
            updateData(advertsInMarket);
    }


    public interface View {
        void showLoadingScreen();

        void updateThumbnails();

        void hideLoadingScreen();

        void showDetailsScreen(String id);
    }
}
