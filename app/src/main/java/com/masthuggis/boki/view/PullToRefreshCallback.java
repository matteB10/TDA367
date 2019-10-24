package com.masthuggis.boki.view;

/**
 * Callback defined to be used by a pull-to-refresh gesture
 * Can be used as a general callback because of the void return-type.
 * Used by ListView.
 * Written by masthuggis
 */
@FunctionalInterface
public interface PullToRefreshCallback {
    void onCallback();
}
