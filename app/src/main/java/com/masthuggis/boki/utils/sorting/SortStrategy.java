package com.masthuggis.boki.utils.sorting;

import com.masthuggis.boki.model.Advertisement;

import java.util.List;

/**
 * Strategy for sorting a list of Advertisments. The name of the sorting strategy, e.g. Highest Price
 * is returned from getName.
 */
public interface SortStrategy {
    List<Advertisement> sort(List<Advertisement> adverts) throws IllegalArgumentException;
    String getName();
}
