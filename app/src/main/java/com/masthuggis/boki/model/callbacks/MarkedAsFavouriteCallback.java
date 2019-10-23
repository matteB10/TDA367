package com.masthuggis.boki.model.callbacks;

@FunctionalInterface
public interface MarkedAsFavouriteCallback {
    void onCallback(boolean markedAsFavourite);
}
