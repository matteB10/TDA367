package com.masthuggis.boki.model.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.util.Iterator;
import java.util.List;

class LatestPublishedSorting implements SortStrategy {
    private static final String NAME = "Senast publicerad";

    @Override
    public Iterator<Advertisement> sort(List<Advertisement> adverts) {
        return adverts.iterator();
    }

    @Override
    public String getName() {
        return NAME;
    }
}
