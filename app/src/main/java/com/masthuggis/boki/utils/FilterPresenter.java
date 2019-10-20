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
        view.addTagToFilters(tag, !isSelected(tag));
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
    public void setUpView(){
        if(filter.getTags().size() > 0){
            setSelectedTags();
        }if(filter.getMaxPrice() != 0){
            view.setMaxPrice(filter.getMaxPrice());
        }
    }
    public void unSetView(){
        view = null;
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
    private void setSelectedTags(){
        for(String tag : filter.getTags()){
            view.addTagToFilters(tag,true);
        }
    }


    public interface View {

        void addTagToFilters(String tag, boolean isSelected);

        void setMaxPrice(int i);

    }
}
