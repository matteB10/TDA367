package com.masthuggis.boki.model.callbacks;

import java.util.List;
@FunctionalInterface

/**
 * A functional interface which only contains one method. Is used to
 * control the flow of the application and extract data in the form of a List containing
 * objects of the String type.
 * Used by DataModel.
 * Written by masthuggis
 */
public interface FavouriteIDsCallback {
    void onCallback(List<String> favouriteIDs);
}
