package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advertisement;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface chatDBCallback {
    void onCallback(List<Map<String,Object>> chatDataList);

}
