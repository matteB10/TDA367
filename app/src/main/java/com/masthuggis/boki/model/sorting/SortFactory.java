package com.masthuggis.boki.model.sorting;

final public class SortFactory {

    public static SortStrategy getAlphabeticalSorting() {
        return new AlphabeticalSorting();
    }

    public static SortStrategy getHighestPriceSorting() {
        return new HightPriceSorting();
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
