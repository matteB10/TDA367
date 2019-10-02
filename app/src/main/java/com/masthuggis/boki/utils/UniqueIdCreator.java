package com.masthuggis.boki.utils;

import java.util.UUID;

public class UniqueIdCreator {


    private UniqueIdCreator(){}


    public static String getUniqueID(){
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

}
