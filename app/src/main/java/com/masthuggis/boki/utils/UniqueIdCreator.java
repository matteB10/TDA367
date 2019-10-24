package com.masthuggis.boki.utils;

import java.util.UUID;

/**
 * Helper class to provide Adverts and Images with unique ID:s
 * Used by BackendWriter, CreateAdPresenter and ImageHandler
 * Written by masthuggis
 */
public class UniqueIdCreator {
    public static String getUniqueID() {
        return UUID.randomUUID().toString();
    }

}
