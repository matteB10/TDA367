package com.masthuggis.boki.model.callbacks;
@FunctionalInterface

/**
 * A functional interface which only contains one method. Is used to
 * control the flow of the application and extract data in the form of
 * objects of the String type.
 * Used by BackendReader and DetailsPresenter.
 * Written by masthuggis
 */
public interface stringCallback {
    void onCallback(String string);
}
