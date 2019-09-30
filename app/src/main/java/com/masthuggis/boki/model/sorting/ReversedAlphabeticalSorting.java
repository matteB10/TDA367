package com.masthuggis.boki.model.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class ReversedAlphabeticalSorting implements SortStrategy {
    private static final String NAME = "Omvänt Alfabetisk (Ö-A)";

    @Override
    public List<Advertisement> sort(List<Advertisement> adverts) {
        if (adverts == null || adverts.isEmpty()) {
            // TODO: implement throws exception instead
            return null;
        }

        return new ArrayList<>(adverts).stream()
                .sorted(Comparator.comparing(Advertisement::getTitle).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return NAME;
    }
}
