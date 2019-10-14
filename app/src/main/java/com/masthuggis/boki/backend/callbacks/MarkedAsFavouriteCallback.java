package com.masthuggis.boki.backend.callbacks;

@FunctionalInterface
public interface MarkedAsFavouriteCallback {
    void onCallback(boolean markedAsFavourite);
}
