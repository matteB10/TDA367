package com.masthuggis.boki.utils.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton that defines what sorting options that are available. Clients get the name of the
 * available sort options, the number of sort options and perform sorting. Client must display
 * the sort options in the same order in order for it to function properly.
 * Used by FavoritesPresenter, HomePresenter andProfilePresenter.
 * written by masthuggis.
 *
 */
public class SortManager {
    private static SortManager instance;
    private final List<SortStrategy> sortStrategies;

    private SortManager() {
        this.sortStrategies = createSortStrategies();
    }

    private List<SortStrategy> createSortStrategies() {
        List<SortStrategy> strategies = new ArrayList<>();
        strategies.add(SortFactory.getLatestPublishedSorting());
        strategies.add(SortFactory.getAlphabeticalSorting());
        strategies.add(SortFactory.getReversedAlphabeticalSorting());
        strategies.add(SortFactory.getLowestPriceSorting());
        strategies.add(SortFactory.getHighestPriceSorting());
        return strategies;
    }

    public static SortManager getInstance() {
        if (instance == null) {
            instance = new SortManager();
        }
        return instance;
    }

    public String[] getSortOptions() {
        String[] arr = new String[sortStrategies.size()];
        for (int i = 0; i < sortStrategies.size(); i++) {
            arr[i] = sortStrategies.get(i).getName();
        }
        return arr;
    }

    public List<Advertisement> sort(int pos, List<Advertisement> advertsToSort) throws NullPointerException {
        return sortStrategies.get(pos).sort(advertsToSort);
    }

    public List<Advertisement> sortWithDefaultSorting(List<Advertisement> advertsToSort) {
        return sortStrategies.get(0).sort(advertsToSort);
    }
}
