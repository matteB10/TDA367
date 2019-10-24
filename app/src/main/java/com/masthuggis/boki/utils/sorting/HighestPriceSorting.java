package com.masthuggis.boki.utils.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Part of a Strategy Pattern. Provides a way to sort by highest price
 * Used by SortFactory.

 * Written by masthuggis
 */
class HighestPriceSorting implements SortStrategy {

    private static final String NAME = "Högsta pris";

    @Override
    public List<Advertisement> sort(List<Advertisement> adverts) throws IllegalArgumentException {
        if (adverts == null || adverts.isEmpty()) {
            throw new NullPointerException();
        }

        return new ArrayList<>(adverts).stream()
                .sorted(Comparator.comparing(Advertisement::getPrice).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return NAME;
    }
}
