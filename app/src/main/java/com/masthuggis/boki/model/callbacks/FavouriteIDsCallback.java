package com.masthuggis.boki.model.callbacks;

import java.util.List;
@FunctionalInterface
public interface FavouriteIDsCallback {
    void onCallback(List<String> favouriteIDs);
}
