package com.masthuggis.boki.utils;

import java.util.UUID;
/**
 *
 * Used by BackendWriter, CreateAdPresenter and ImageHandler
 * Written by masthuggis
 */
public class UniqueIdCreator {


    private UniqueIdCreator(){}


    public static String getUniqueID(){
        return UUID.randomUUID().toString();
    }

}
