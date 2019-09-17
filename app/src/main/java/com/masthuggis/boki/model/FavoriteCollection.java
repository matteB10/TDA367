package com.masthuggis.boki.model;

import java.util.List;

public class FavoriteCollection implements iFavoriteCollection {
   private List<Advertisement> advertisements;

    public List<Advertisement> getAdvertisements() {
        return this.advertisements;
    }
}
