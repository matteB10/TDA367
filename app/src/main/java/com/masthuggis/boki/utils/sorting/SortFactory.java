package com.masthuggis.boki.utils.sorting;
/**
 * Factory class for different kinds of SortStrategies.
 * Used by SortManager
 * Written by masthuggis
 */
final public class SortFactory {

    public static SortStrategy getAlphabeticalSorting() {
        return new AlphabeticalSorting();
    }

    public static SortStrategy getHighestPriceSorting() {
        return new HighestPriceSorting();
    }

    public static SortStrategy getLatestPublishedSorting() {
        return new LatestPublishedSorting();
    }

    public static SortStrategy getLowestPriceSorting() {
        return new LowestPriceSorting();
    }

    public static SortStrategy getReversedAlphabeticalSorting() {
        return new ReversedAlphabeticalSorting();
    }

}
