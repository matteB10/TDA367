package com.masthuggis.boki.backend.callbacks;

import java.util.List;
@FunctionalInterface
public interface FavouriteIDsCallback {
    void onCallback(List<String> favouriteIDs);
}
