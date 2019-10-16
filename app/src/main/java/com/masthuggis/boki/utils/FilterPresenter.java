package com.masthuggis.boki.utils;

import java.util.List;

public class FilterPresenter {

    private View view;
    private static Filter filter = Filter.getInstance();
    private List<String> preDefTags;


    public FilterPresenter(FilterPresenter.View view) {
        this.view = view;
    }

    public void tagsChanged(String tag) {
        if (isSelected(tag)) {
            filter.addTag(tag);
        } else {
            filter.removeTag(tag);
        }
        view.addTag(tag, !isSelected(tag));
    }

    public List<String> getPreDefTags() {
        return preDefTags;
    }

    public List<String> getActiveTags() {
        return filter.getTags();
    }

    public int getMaxPrice() {
        return filter.getMaxPrice();
    }

    public void maxPriceChanged(int i) {
        filter.setMaxPrice(i);
        view.setMaxPrice(i);
    }

    public void setPreDefTags(List<String> strTags) {
        this.preDefTags = strTags;
    }

    private boolean isSelected(String tag) {
        for (String t : filter.getTags()) {
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
