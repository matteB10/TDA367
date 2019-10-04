package com.masthuggis.boki.model.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class ReversedAlphabeticalSorting implements SortStrategy {
    private static final String NAME = "Omvänt Alfabetisk (Ö-A)";

    @Override
    public List<Advertisement> sort(List<Advertisement> adverts) throws IllegalArgumentException {
        if (adverts == null || adverts.isEmpty()) {
            throw new IllegalArgumentException();
        }

        List<Advertisement> sorted = new ArrayList<>(adverts).stream()
                                        .sorted((adOne, adTwo) -> adOne.getTitle().toLowerCase().compareTo(adTwo.getTitle().toLowerCase()))
                                        .collect(Collectors.toList());
        Collections.reverse(sorted);
        return sorted;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
