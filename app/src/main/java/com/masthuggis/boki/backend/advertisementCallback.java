package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advertisement;

import java.util.List;

@FunctionalInterface
public interface advertisementCallback {
    void onCallback(List<Advertisement> advertisements);
}
