package com.masthuggis.boki.utils;

import com.masthuggis.boki.backend.PerformedSearchCallback;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public class SearchHelper {

    private List<Advertisement> currentSearchRes = new ArrayList<>();

    public SearchHelper() {
    }

    public void search(String query, PerformedSearchCallback callback) {

        Thread thread = new Thread(() -> DataModel.getInstance().fetchAllAdverts(advertisements -> {
            List<Advertisement> tempList = new ArrayList<>();
            if (advertisements != null) {
                tempList = new ArrayList<>(advertisements); //temporary list of all adsRefreshes the list so it accurately reflects adverts in firebase
            }
            List<Advertisement> searchRes = new ArrayList<>(); //new list with searchresults
            String queryStr = query.toLowerCase().trim();

            searchMatchTitle(searchRes, queryStr, tempList); //search if query matches title. run first, highest priority
            searchMatchTags(searchRes, queryStr, tempList); //search if query matches tag. run second, second highest priority
            searchContainsTitle(searchRes, queryStr, tempList); //search if title contains query.
            searchContainsTags(searchRes, queryStr, tempList); //search if at least one of ads tags matches query.

            callback.onCallback(searchRes);
        }));
        thread.start();
    }

    /**
     *
     * @return searchResult
     */

    public List<Advertisement> getResultFromSearch() {
        return currentSearchRes;
    }
    /**
     * Checks if title matches search string and ads that Advert to search result
     * if true.
     *
     * @param searchRes list to add matching advert
     * @param query     the search string
     * @param ads       list of adverts
     */
    private void searchMatchTitle(List<Advertisement> searchRes, String
            query, List<Advertisement> ads) {
        Advertisement ad;
        for (int i = 0; i < ads.size(); i++) {
            ad = ads.get(i);
            String title = ad.getTitle().toLowerCase().trim();

            if (titleStartsWithQuery(title, query)) {
                searchRes.add(ad);
                ads.remove(ad);

            }
        }
    }

    /**
     * Checks if at least one tag matches search string and ads that Advert to search result
     * if true.
     *
     * @param searchRes list to add matching advert
     * @param query     the search string
     * @param ads       list of adverts
     */

    private void searchMatchTags(List<Advertisement> searchRes, String
            query, List<Advertisement> ads) {
        Advertisement ad;
        for (int i = 0; i < ads.size(); i++) {
            ad = ads.get(i);
            if (tagsStartWithQuery(ad.getTags(), query)) {
                searchRes.add(ad);
                ads.remove(ad);
            }
        }
    }

    /**
     * Checks if title contains search string and ads Advert to search result
     * if true.
     *
     * @param searchRes list to add matching advert
     * @param query     the search string
     * @param ads       list of adverts
     */

    private void searchContainsTitle(List<Advertisement> searchRes, String
            query, List<Advertisement> ads) {
        Advertisement ad;
        for (int i = 0; i < ads.size(); i++) {
            ad = ads.get(i);
            if (ad.getTitle().toLowerCase().trim().contains(query)) {
                searchRes.add(ad);
                ads.remove(ad);
            }
        }
    }

    /**
     * @param searchRes
     * @param query
     * @param ads
     */

    private void searchContainsTags(List<Advertisement> searchRes, String
            query, List<Advertisement> ads) {
        Advertisement ad;
        for (int i = 0; i < ads.size(); i++) {
            ad = ads.get(i);
            if (tagsContainsQuery(ad.getTags(), query)) {
                searchRes.add(ad);
                ads.remove(ad);
            }
        }
    }

    /**
     * @param title
     * @param query search string
     * @return true if title matches query
     */

    private boolean titleStartsWithQuery(String title, String query) {
        if (title.length() >= query.length()) {
            String titleStart = title.substring(0, query.length());
            return (titleStart.equals(query));
        }
        return false;
    }

    /**
     * @param tags  list of an ads tags
     * @param query search string
     * @return true if at least one tag matches the query string
     */

    private boolean tagsStartWithQuery(List<String> tags, String query) {
        String substring;
        for (String tag : tags) {
            if (tag.trim().length() >= query.length()) {
                substring = tag.toLowerCase().trim().substring(0, query.length());
                if (substring.equals(query)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param tags  list of an ads tags
     * @param query search string
     * @return true if at least one tag contains the query string
     */
    private boolean tagsContainsQuery(List<String> tags, String query) {
        for (String tag : tags) {
            if (tag.toLowerCase().trim().contains(query)) {
                return true;
            }
        }
        return false;
    }
}
