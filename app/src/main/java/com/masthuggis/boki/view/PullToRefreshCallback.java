package com.masthuggis.boki.view;

/**
 * Callback used when user takes action to refresh view.
 * Used by ListView.
 * Written by masthuggis
 */
@FunctionalInterface
public interface PullToRefreshCallback {
    void onCallback();
}
