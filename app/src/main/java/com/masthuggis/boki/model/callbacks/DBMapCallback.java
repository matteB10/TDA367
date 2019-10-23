package com.masthuggis.boki.model.callbacks;

import java.util.Map;
/**
 * A functional interface which only contains one method. Is used to
 * control the flow of the application and extract data in the form of a
 * Map with the key, value pair of String and Object- types.
 * Used by repositories to extract data from backend.
 *Written by masthuggis
 */
@FunctionalInterface
public interface DBMapCallback {
        void onCallBack(Map<String, Object> dataMap);
    }
