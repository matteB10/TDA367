package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.sorting.SortManager;
import com.masthuggis.boki.utils.SearchHelper;

import java.util.List;

/**
 * HomePresenter is the presenter class for the view called HomeFragment.
 * <p>
 * Presenter handling the home/start view. It handles a view that implements both AdvertsPresenterView
 * interface. It displays all the market adverts with the option to sort and search.
 * It is an observer of the market so it can update its data accordingly.
 */
public final class HomePresenter extends AdvertsPresenter {

    private final AdvertsPresenterView view;
    private int selectedSortOption = 0;

    public HomePresenter(AdvertsPresenterView view, DataModel dataModel) {
        super(view, dataModel);
        this.view = view;
    }

    @Override
    public List<Advertisement> getData() {
        return dataModel.getAllAdverts();
    }

    /**
     * When a search is performed it will display all the available adverts if the search field
     * is empty and ask for the search results if the query is not empty.
     *
     * @param query
     */
    public void searchPerformed(String query) {
        view.showLoadingScreen();

        if(!searchFieldIsEmpty(query)){
            SearchHelper.search(query, searchResult -> super.updateAdverts(searchResult));
        }
        /*
        if (searchFieldIsEmpty(query)) {
            super.updateAdverts();
        } else {
            SearchHelper.search(query, searchResult -> super.updateAdverts(searchResult));
        }*/
    }

    /**
     * Apply filters, display search result
     * @param price maxPrice
     * @param tags filter tags
     */
    public void addFilters(int price, List<String> tags){
        SearchHelper.addFilters(price, tags, searchResult -> super.updateAdverts(searchResult));
    }

    /**
     * Sorts using the selected sort option.
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
        super.updateAdverts();
    }

    private boolean searchFieldIsEmpty(String query) {
        return query.equals("");
    }

     public void updateFromUserInteraction() {
        selectedSortOption = 0;
        super.updateAdverts();


    }
}
