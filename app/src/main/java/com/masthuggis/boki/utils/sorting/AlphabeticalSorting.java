package com.masthuggis.boki.utils.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Part of a Strategy Pattern. Provides a way to sort alphabetically
 * Used by SortFactory.
 * Written by masthuggis
 */
class AlphabeticalSorting implements SortStrategy {
    private static final String NAME = "Alfabetisk (A-Ö)";

    @Override
    public List<Advertisement> sort(List<Advertisement> adverts) throws IllegalArgumentException {
        if (adverts == null || adverts.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return new ArrayList<>(adverts).stream()
                .sorted((adOne, adTwo) -> adOne.getTitle().toLowerCase().compareTo(adTwo.getTitle().toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return NAME;
    }
}
