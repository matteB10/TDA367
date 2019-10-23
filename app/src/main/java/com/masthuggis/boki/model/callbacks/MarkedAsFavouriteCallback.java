package com.masthuggis.boki.model.callbacks;

@FunctionalInterface

/**
 * A functional interface which only contains one method. Is used to
 * control the flow of the application and extract data in the form of a List containing
 * boolean type.
 * Used by DataModel
 * Written by masthuggis
 */
public interface MarkedAsFavouriteCallback {
    void onCallback(boolean markedAsFavourite);
}
