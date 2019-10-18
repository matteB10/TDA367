package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.sorting.SortManager;
import com.masthuggis.boki.utils.Filter;
import com.masthuggis.boki.utils.SearchHelper;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.ArrayList;
import java.util.List;

/**
 * HomePresenter is the presenter class for the view called HomeFragment.
 * <p>
 * Presenter handling the home/start view. It handles a view that implements both ListPresenterView
 * interface. It displays all the market adverts with the option to sort and search.
 * It is an observer of the market so it can update its data accordingly.
 */
public final class HomePresenter extends AdvertsPresenter<Advertisement, ThumbnailView> {

    private final ListPresenterView view;
    private int selectedSortOption = 0;

    public HomePresenter(ListPresenterView view, DataModel dataModel) {
        super(view, dataModel);
        this.view = view;
        setCurrentDisplayedAds(applyFilters(dataModel.getAllAdverts()));
    }


    @Override
    public List<Advertisement> getData() {
        if (getCurrentDisplayedAds().size() == 0 && !SearchHelper.isActiveSearch()) {
            if (!SearchHelper.isActiveFilters())
                return new ArrayList<>(dataModel.getAllAdverts());

        }
        if (SearchHelper.isActiveFilters()) {
            return applyFilters(new ArrayList<>(dataModel.getAllAdverts()));
        }
        return getCurrentDisplayedAds();
    }


    /**
     * When a search is performed it will display all the available adverts if the search field
     * is empty and ask for the search results if the query is not empty.
     *
     * @param query
     */
    public void searchPerformed(String query) {
        view.showLoadingScreen();
        if (!searchFieldIsEmpty(query)) {
            updateAdverts(SearchHelper.search(query, getData()), false);
        }else{
            updateAdverts(SearchHelper.search(query, getData()), true);
        }
    }

    /**
     * Apply filters if filters are set
     */
    public List<Advertisement> applyFilters(List<Advertisement> ads) {
        int maxPrice = Filter.getInstance().getMaxPrice();
        List<String> filterTags = Filter.getInstance().getTags();
        return (SearchHelper.filters(maxPrice, filterTags, ads));

    }


    /**
     * If boolean is true,  sort using the selected sort option
     *
     * @param adverts
     * @return
     */
    @Override
    public List<Advertisement> sort(List<Advertisement> adverts) {
        return SortManager.getInstance().sort(selectedSortOption, adverts);
    }

    public String[] getSortOptions() {
        return SortManager.getInstance().getSortOptions();
    }

    /**
     * Whenever a new sort option is selected the view is updated.
     *
     * @param pos
     */
    public void sortOptionSelected(int pos) {
        selectedSortOption = pos;
        updateAdverts(getData());
    }

    private boolean searchFieldIsEmpty(String query) {
        return query.length() == 0;
    }

    public void updateFromUserInteraction() {
        super.updateAdverts();
    }

    @Override
    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
        AdvertsPresenterHelper.onBindThumbnailViewAtPosition(position, thumbnailView, getCurrentDisplayedAds());
    }
}
