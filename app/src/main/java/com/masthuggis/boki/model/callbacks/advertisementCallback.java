package com.masthuggis.boki.model.callbacks;

import com.masthuggis.boki.model.Advertisement;

import java.util.List;

@FunctionalInterface
public interface advertisementCallback {
    void onCallback(List<Advertisement> advertisements);
}
