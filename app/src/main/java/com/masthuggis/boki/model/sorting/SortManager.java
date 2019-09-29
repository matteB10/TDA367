package com.masthuggis.boki.model.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.Iterator;
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
        strategies.add(new HightPriceSorting());
        strategies.add(new LowestPriceSorting());
        strategies.add(new LatestPublishedSorting());
        return strategies;
    }

    public static SortManager getInstance() {
        if (instance == null) {
            instance = new SortManager();
        }
        return instance;
    }

    public int getNumSortOptions() {
        return sortStrategies.size();
    }

    public Iterator<String> getSortOptions() {
        List<String> sortOptionNames = new ArrayList<>();
        for (SortStrategy strategy: sortStrategies) {
            sortOptionNames.add(strategy.getName());
        }
        return sortOptionNames.iterator();
    }

    public Iterator<Advertisement> sort(int pos, List<Advertisement> advertsToSort) {
        return sortStrategies.get(pos).sort(advertsToSort);
    }
}
