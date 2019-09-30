package com.masthuggis.boki.model.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

class LatestPublishedSorting implements SortStrategy {
    private static final String NAME = "Senast publicerad";

    @Override
    public Iterator<Advertisement> sort(List<Advertisement> adverts) {
        if (adverts == null || adverts.isEmpty()) {
            // TODO: implement throws exception instead
            return null;
        }

        return new ArrayList<>(adverts).stream()
                .sorted(Comparator.comparing(Advertisement::getDatePublished))
                .collect(Collectors.toList())
                .iterator();
    }

    @Override
    public String getName() {
        return NAME;
    }
}
