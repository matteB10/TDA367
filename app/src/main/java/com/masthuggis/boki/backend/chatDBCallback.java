package com.masthuggis.boki.backend;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface chatDBCallback {
    void onCallback(List<Map<String,Object>> chatDataList);

}
