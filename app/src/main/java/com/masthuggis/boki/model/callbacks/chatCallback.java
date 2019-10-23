package com.masthuggis.boki.model.callbacks;

import com.masthuggis.boki.model.iChat;

import java.util.List;
/**
 * A functional interface which only contains one method. Is used to
 * control the flow of the application and extract data in the form of a List containing
 * objects of the iChat type.
 * Used by DataModel
 * Written by masthuggis
 */
@FunctionalInterface
public interface chatCallback {
    void onCallback(List<iChat> chatsList);
}
