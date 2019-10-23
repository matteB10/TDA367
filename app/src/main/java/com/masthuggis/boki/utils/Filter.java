package com.masthuggis.boki.utils;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * Used by FilterPresenter and HomePresenter.
 * Written by masthuggis
 */
public class Filter {

    private List<String> tags = new ArrayList<>();
    private int maxPrice = 500; //max value on filtering screen
    private static Filter filter;

    private Filter() {
    }

    public static Filter getInstance() {
        if (filter == null) {
            filter = new Filter();
        }
        return filter;
    }


    public List<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }
}
