package com.masthuggis.boki.model.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

class LowestPriceSorting implements SortStrategy {
    private static final String NAME = "Lägsta pris";

    @Override
    public Iterator<Advertisement> sort(List<Advertisement> adverts) {
        return new ArrayList<>(adverts).stream()
                .sorted(Comparator.comparing(Advertisement::getPrice).reversed())
                .collect(Collectors.toList())
                .iterator();
    }

    @Override
    public String getName() {
        return NAME;
    }
}
