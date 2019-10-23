package com.masthuggis.boki.utils.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * Used by SortFactory.
 * Written by masthuggis
 */
class LowestPriceSorting implements SortStrategy {
    private static final String NAME = "Lägsta pris";

    @Override
    public List<Advertisement> sort(List<Advertisement> adverts) throws IllegalArgumentException {
        if (adverts == null || adverts.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return new ArrayList<>(adverts).stream()
                .sorted(Comparator.comparing(Advertisement::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return NAME;
    }
}
