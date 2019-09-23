package com.masthuggis.boki.utils;

import java.util.UUID;

public class UniqueIdCreator {

    private static UniqueIdCreator idCreator;

    private UniqueIdCreator(){}

    public static UniqueIdCreator getInstance(){
        if(idCreator == null){
            idCreator = new UniqueIdCreator();
        }
        return idCreator;
    }

    public static String getUniqueID(){
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

}
