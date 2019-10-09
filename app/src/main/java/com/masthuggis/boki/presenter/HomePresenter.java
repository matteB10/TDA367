package com.masthuggis.boki.presenter;

import android.os.Handler;

import com.masthuggis.boki.backend.MockRepository;
import com.masthuggis.boki.backend.PerformedSearchCallback;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.observers.AdvertisementObserver;
import com.masthuggis.boki.model.sorting.SortManager;
import com.masthuggis.boki.utils.ClickDelayHelper;
import com.masthuggis.boki.utils.SearchHelper;
import com.masthuggis.boki.utils.StylingHelper;
import com.masthuggis.boki.view.SearchCallback;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.ArrayList;
import java.util.List;

/**
 * HomePresenter is the presenter class for the view called HomeFragment.
 */
public class HomePresenter implements IProductsPresenter, AdvertisementObserver {
    private final View view;
    private final SortManager sortManager;
    private List<Advertisement> adverts;
    private int selectedSortOption = 0;


    public HomePresenter(View view) {
        this.view = view;
        this.sortManager = SortManager.getInstance();

        this.view.showLoadingScreen();

        // Used when using local JSON, comment if using firebase
        //useTestData();

        // If using firebase uncommment line below
        getData();
        DataModel.getInstance().addAdvertisementObserver(this);
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
        if (adverts == null) {
            return;
        }

        this.adverts = new ArrayList<>(adverts);
        sort(selectedSortOption);
        this.view.hideLoadingScreen();
        this.view.updateThumbnails();
    }

    //TODO: fix better solution for this, may be needed when filtering later
    private void updateDataWithoutSorting(List<Advertisement> adverts) {
        if (adverts == null) {
            return;
        }

        this.adverts = new ArrayList<>(adverts);
        this.view.hideLoadingScreen();
        this.view.updateThumbnails();
    }

    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
        if (adverts.size() < position || adverts == null)
            return;
        Advertisement a = adverts.get(position);
        thumbnailView.setId(a.getUniqueID());
        thumbnailView.setTitle(a.getTitle());
        thumbnailView.setPrice(a.getPrice());
        setCondition(a, thumbnailView);
        if (a.getImageUrl() != null) {
            thumbnailView.setImageURL(a.getImageUrl());
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
      return ClickDelayHelper.canProceedWithTapAction();
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
        selectedSortOption = pos;
        updateData(adverts);
    }

    private void sort(int pos) {
        if (adverts == null || adverts.size() == 0) {
            return;
        }

        adverts = sortManager.sort(pos, adverts);
    }


    //Search the advertisements shown to the user by if their title or tags matches/contains the given query
    public void search(String query, SearchCallback callback) {
        view.showLoadingScreen();
        if(query.equals("")) {
            getData(); //if query is empty string, update view use standard sorting //TODO: Maybe rename method
        }else {
            SearchHelper.search(query, new PerformedSearchCallback() {
                @Override
                public void onCallback(List<Advertisement> searchRes) {
                    updateDataWithoutSorting(searchRes);
                    callback.onCallback();
                }
            });
        }

    }


    @Override
    public void onAdvertisementsUpdated() {
        updateData(DataModel.getInstance().getAllAds());
    }

    public interface View {
        void showLoadingScreen();

        void updateThumbnails();

        void hideLoadingScreen();

        void showDetailsScreen(String id);
    }
}
