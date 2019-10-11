package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.callbacks.advertisementCallback;
import com.masthuggis.boki.backend.callbacks.advertisementCallback;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.observers.AdvertisementObserver;
import com.masthuggis.boki.model.sorting.SortManager;
import com.masthuggis.boki.utils.SearchHelper;

import java.util.List;

/**
 * HomePresenter is the presenter class for the view called HomeFragment.
 */
public final class HomePresenter extends AdvertsPresenter implements AdvertisementObserver {

    private final AdvertsPresenterView view;
    private int selectedSortOption = 0;
    private DataModel dataModel;

    public HomePresenter(AdvertsPresenterView view, DataModel dataModel) {
        super(view);
        this.dataModel = dataModel;
        this.view = view;
        super.updateData();
        this.dataModel.addMarketAdvertisementObserver(this);
    }

    @Override
    public void getData(advertisementCallback advertisementCallback) {
        dataModel.fetchAllAdverts(advertisementCallback::onCallback);
    }

    //Search the advertisements shown to the user by if their title or tags matches/contains the given query
    public void searchPerformed(String query) {
        view.showLoadingScreen();
        if (query.equals("")) {
            super.updateData(); //if query is empty string, update view use standard sorting
        } else {
            SearchHelper.search(query, searchResult -> super.updateData(searchResult));
        }
    }

    @Override
    public void onAdvertisementsUpdated() {
        super.updateData();
    }

    @Override
    public List<Advertisement> sort(List<Advertisement> adverts) {
        return SortManager.getInstance().sort(selectedSortOption, adverts);
    }

    public String[] getSortOptions() {
        return convertListToArray(SortManager.getInstance().getSortOptions());
    }

    public void sortOptionSelected(int pos) {
        selectedSortOption = pos;
        super.updateData();
    }

    private String[] convertListToArray(List<String> list) {
        String arr[] = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

}
