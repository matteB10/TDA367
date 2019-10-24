package com.masthuggis.boki.presenter;

import com.masthuggis.boki.utils.Filter;

import java.util.List;

/**
 * Handle user input from filter fragment, responsible for setting up the view with correct
 * values if the user have active filters.
 * Used by FilterFragment.
 * Written by masthuggis
 */
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
        view.setUpFilters(tag, !isSelected(tag));
    }

    /**
     * Get current list of available filter tags
     * @return List of tags
     */
    public List<String> getPreDefTags() {
        return preDefTags;
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
            view.setUpFilters(tag,true);
        }
    }


    public interface View {

        void setUpFilters(String tag, boolean isSelected);

        void setMaxPrice(int i);

    }
}
