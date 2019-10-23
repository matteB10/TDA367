package com.masthuggis.boki.model.callbacks;

import java.util.List;
import java.util.Map;
/**
 * A functional interface which only contains one method. Is used to
 * control the flow of the application and extract data in the form of a List containing
 * objects of the Map type with the key, value pair of String and Object- types.
 * Used by repositories and backend to extract data from database.
 * Written by masthuggis
 */
@FunctionalInterface
public interface DBCallback {
    void onCallBack(List<Map<String, Object>> dataList);
}
