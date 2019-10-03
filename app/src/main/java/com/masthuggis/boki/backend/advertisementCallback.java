package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.Chat;

import java.util.List;

@FunctionalInterface
public interface advertisementCallback {
    void onCallback(List<Advertisement> advertisements);
}
