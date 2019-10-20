package com.masthuggis.boki.utils;

import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class managing searching ads with an input searchPerformed string.
 */

public class SearchHelper {

    private static boolean hasActiveSearch = false;
    private static boolean hasActiveFilters = false;


    private SearchHelper() {
    }


    public static List<Advertisement> search(String query, List<Advertisement> advertisements) {
        List<Advertisement> searchRes = new ArrayList<>(); //new list with search results
        String queryStr = query.toLowerCase().trim();
        hasActiveSearch = query.length() > 0; //if empty query, activeSearch set to 0.

        if (advertisements != null) {
            performSearch(searchRes, queryStr, advertisements);
        }
        return searchRes;
    }

    /**
     * Use filter strings to perform search, add all search results
     *
     * @param filters
     */

    public static List<Advertisement> filters(int price, List<String> filters, List<Advertisement> advertisements) {
        hasActiveFilters = true; //price filter is always activated if user clicked on filtering screen once
        List<Advertisement> searchRes = new ArrayList<>();
        List<Advertisement> adsToBeSearched = new ArrayList<>(advertisements);
        if (price == 500) {
            price = FormHelper.getInstance().getValidMaxPrice(); //max price filter is 500+, actual maxPrice getting from formhelper;
        }
        if (filters.size() == 0) {
            searchRes = addPriceFilter(adsToBeSearched, price);
        } else {
            searchRes = filterWithTagsAndPrice(adsToBeSearched, filters, price);
        }
        return searchRes;

    }

    /**
     * If active filter tags, filter both tags and price
     */
    private static List<Advertisement> filterWithTagsAndPrice(List<Advertisement> adsToBeSearched, List<String> filters, int price) {
        List<Advertisement> searchRes = new ArrayList<>();
        for (String filter : filters) {

            filter = filter.trim().toLowerCase();
            adsToBeSearched = performSearch(searchRes, filter, adsToBeSearched);
        }
        searchRes = addPriceFilter(searchRes, price);
        return searchRes;
    }

    /**
     * Call all search methods
     *
     * @param searchRes
     * @param queryStr
     * @param listTosearch
     */
    private static List<Advertisement> performSearch(List<Advertisement> searchRes, String queryStr, List<Advertisement> listTosearch) {
        listTosearch = searchMatchTitle(searchRes, queryStr, listTosearch); //searchPerformed if query matches title. run first, highest priority
        if (queryStr.length() > 3) { //not a relevant search result if title or tag contains less than 4 characters in query
            listTosearch = searchMatchTags(searchRes, queryStr, listTosearch); //searchPerformed if query matches tag. run second, second highest priority
            listTosearch = searchContainsTitle(searchRes, queryStr, listTosearch); //searchPerformed if title contains query.
            listTosearch = searchContainsTags(searchRes, queryStr, listTosearch); //searchPerformed if at least one of ads tags matches query.
        }
        return listTosearch;
    }

    /**
     * Checks if title matches searchPerformed string and ads that Advert to searchPerformed result
     * if true.
     *
     * @param searchRes list to add matching advert
     * @param query     the searchPerformed string
     * @param ads       list of adverts
     */
    private static List<Advertisement> searchMatchTitle(List<Advertisement> searchRes, String
            query, List<Advertisement> ads) {
        List<Advertisement> listToSearch = new ArrayList<>(ads);
        Advertisement ad;
        int endLoop = ads.size();
        for (int i = 0; i < endLoop; i++) {
            ad = ads.get(i);
            String title = ad.getTitle().toLowerCase().trim();

            if (titleStartsWithQuery(title, query)) {
                searchRes.add(ad);
                listToSearch.remove(ad);

            }
        }
        return listToSearch;
    }

    /**
     * Checks if at least one tag matches searchPerformed string and ads that Advert to searchPerformed result
     * if true.
     *
     * @param searchRes list to add matching advert
     * @param query     the searchPerformed string
     * @param ads       list of adverts
     */

    private static List<Advertisement> searchMatchTags(List<Advertisement> searchRes, String query, List<Advertisement> ads) {
        Advertisement ad;
        List<Advertisement> listToSearch = new ArrayList<>(ads);
        int endLoop = ads.size();
        for (int i = 0; i < endLoop; i++) {
            ad = ads.get(i);
            if (tagsStartWithQuery(ad.getTags(), query)) {
                searchRes.add(ad);
                listToSearch.remove(ad);
            }
        }
        return listToSearch;
    }

    /**
     * Checks if title contains searchPerformed string and ads Advert to searchPerformed result
     * if true.
     *
     * @param searchRes list to add matching advert
     * @param query     the searchPerformed string
     * @param ads       list of adverts
     */

    private static List<Advertisement> searchContainsTitle(List<Advertisement> searchRes, String query, List<Advertisement> ads) {
        Advertisement ad;
        List<Advertisement> listToSearch = new ArrayList<>(ads);
        int endLoop = ads.size();
        for (int i = 0; i < endLoop; i++) {
            ad = ads.get(i);
            if (ad.getTitle().toLowerCase().trim().contains(query)) {
                searchRes.add(ad);
                listToSearch.remove(ad);
            }
        }
        return listToSearch;
    }

    /**
     * @param searchRes
     * @param query
     * @param ads
     */

    private static List<Advertisement> searchContainsTags(List<Advertisement> searchRes, String query, List<Advertisement> ads) {
        Advertisement ad;
        List<Advertisement> listToSearch = new ArrayList<>(ads);
        int endLoop = ads.size();
        for (int i = 0; i < endLoop; i++) {
            ad = ads.get(i);
            if (tagsContainsQuery(ad.getTags(), query)) {
                searchRes.add(ad);
                listToSearch.remove(ad);
            }
        }
        return listToSearch;
    }

    /**
     * @param title
     * @param query searchPerformed string
     * @return true if title matches query
     */

    private static boolean titleStartsWithQuery(String title, String query) {
        if (title.length() >= query.length()) {
            String titleStart = title.substring(0, query.length());
            return (titleStart.equals(query));
        }
        return false;
    }

    /**
     * @param tags  list of an ads tags
     * @param query searchPerformed string
     * @return true if at least one tag matches the query string
     */

    private static boolean tagsStartWithQuery(List<String> tags, String query) {
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
     * @param query searchPerformed string
     * @return true if at least one tag contains the query string
     */
    private static boolean tagsContainsQuery(List<String> tags, String query) {
        for (String tag : tags) {
            if (tag.toLowerCase().trim().contains(query)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Search through already added search results to add price filter
     */
    private static List<Advertisement> addPriceFilter(List<Advertisement> searchRes, int maxPrice) {
        Advertisement ad;
        List<Advertisement> searchResult = new ArrayList<>();
        for (int i = 0; i < searchRes.size(); i++) {
            ad = searchRes.get(i);
            if (maxPrice > ad.getPrice()) {
                searchResult.add(ad);
            }
        }
        return searchResult;
    }

    public static boolean isActiveSearch() {
        return hasActiveSearch;
    }

    public static boolean isActiveFilters() {
        return hasActiveFilters;
    }
}
