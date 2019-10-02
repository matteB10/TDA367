package com.masthuggis.boki.model.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class LatestPublishedSorting implements SortStrategy {
    private static final String NAME = "Senast publicerad";

    @Override
    public List<Advertisement> sort(List<Advertisement> adverts) {
        if (adverts == null || adverts.isEmpty()) {
            // TODO: implement throws exception instead
            return null;
        }

        return new ArrayList<>(adverts).stream()
                .sorted((adOne, adTwo) -> convertDateToInt(adOne.getDatePublished()) - convertDateToInt(adTwo.getDatePublished()))
                .collect(Collectors.toList());
    }

    private int convertDateToInt(String date) {
        String year, month, day, time;
        date = date
                .replace("/", "")
                .replace(":","")
                .replace(" ", "");
        day = date.substring(0, 2);
        month = date.substring(2, 4);
        year = date.substring(4, 6);
        time = date.substring(6,8);
        int convertedToInt = Integer.parseInt(year + month + day + time);
        return convertedToInt;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
