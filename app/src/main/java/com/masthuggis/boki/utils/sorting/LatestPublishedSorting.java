package com.masthuggis.boki.utils.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Used by SortFactory.

 * Written by masthuggis
 */
class LatestPublishedSorting implements SortStrategy {
    private static final String NAME = "Senast publicerad";

    @Override
    public List<Advertisement> sort(List<Advertisement> adverts) throws IllegalArgumentException {
        if (adverts == null || adverts.isEmpty()) {
            throw new IllegalArgumentException();
        }

        List<Advertisement> sorted = new ArrayList<>(adverts).stream()
                                        .sorted((adOne, adTwo) -> isDateOneNewerThanDateTwo(
                                                convertDateToLong(adOne.getDatePublished()),
                                                convertDateToLong(adTwo.getDatePublished()))
                                        ).collect(Collectors.toList());
        Collections.reverse(sorted);
        return sorted;
    }

    private long convertDateToLong(String date) {
        String year, month, day, time;
        date = date
                .replace("/", "")
                .replace(":","")
                .replace(" ", "");
        day = date.substring(0, 2);
        month = date.substring(2, 4);
        year = date.substring(4, 6);
        time = date.substring(6,12);
        long convertedToInt = Long.parseLong(year + month + day + time);
        return convertedToInt;
    }

    /**
     * Returns 1 if if dateOne is larger than two, else it returns -1.
     * @param dateOne
     * @param dateTwo
     * @return
     */
    private int isDateOneNewerThanDateTwo(long dateOne, long dateTwo) {
        if ((dateOne - dateTwo) > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String getName() {
        return NAME;
    }
}
