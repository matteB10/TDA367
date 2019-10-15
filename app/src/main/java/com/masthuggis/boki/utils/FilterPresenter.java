package com.masthuggis.boki.utils;

import java.util.ArrayList;
import java.util.List;

public class FilterPresenter {

    View view;
    List<String> preDefTags = new ArrayList<>();
    List<String> activeTags = new ArrayList<>();
    int maxPrice;


    public FilterPresenter(FilterPresenter.View view) {
        this.view = view;
    }

    public void tagsChanged(String tag) {
        if (isSelected(tag)) {
            activeTags.add(tag);
        } else {
            activeTags.remove(tag);
        }
        view.addTag(tag, !isSelected(tag));
    }

    public List<String> getPreDefTags() {
        return preDefTags;
    }

    public List<String> getActiveTags() {
        return activeTags;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void maxPriceChanged(int i) {
        maxPrice = i;
        view.setMaxPrice(i);
    }

    public void setPreDefTags(List<String> strTags) {
        this.preDefTags = strTags;
    }

    private boolean isSelected(String tag) {
        for (String t : activeTags) {
            if (t.equals(tag)) {
                return false;
            }
        }
        return true;
    }


    public interface View {

        void addTag(String tag, boolean isSelected);

        void setMaxPrice(int i);

    }
}
