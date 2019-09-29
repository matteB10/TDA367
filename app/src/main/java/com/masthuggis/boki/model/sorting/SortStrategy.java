package com.masthuggis.boki.model.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.util.Iterator;
import java.util.List;

/**
 * Strategy for sorting a list of Advertisments. The name of the sorting strategy, e.g. Highest Price
 * is returned from getName.
 */
public interface SortStrategy {
    Iterator<Advertisement> sort(List<Advertisement> adverts);
    String getName();
}
