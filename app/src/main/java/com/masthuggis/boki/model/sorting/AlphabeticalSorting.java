package com.masthuggis.boki.model.sorting;

import android.util.Log;

import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class AlphabeticalSorting implements SortStrategy {
    private static final String NAME = "Alfabetisk (A-Ö)";

    @Override
    public List<Advertisement> sort(List<Advertisement> adverts) {
        if (adverts == null || adverts.isEmpty()) {
            // TODO: implement throws exception instead
            return null;
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
