package com.masthuggis.boki.model.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton that defines what sorting options that are available. Clients get the name of the
 * available sort options, the number of sort options and perform sorting. Client must display
 * the sort options in the same order in order for it to function properly.
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

    public List<String> getSortOptions() {
        List<String> sortOptionNames = new ArrayList<>();
        for (SortStrategy strategy: sortStrategies) {
            sortOptionNames.add(strategy.getName());
        }
        return sortOptionNames;
    }

    public List<Advertisement> sort(int pos, List<Advertisement> advertsToSort) throws NullPointerException {
        return sortStrategies.get(pos).sort(advertsToSort);
    }
}
