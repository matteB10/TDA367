package com.masthuggis.boki.backend;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface advertisementDBCallback {
    void onCallBack(List<Map<String, Object>> advertDataList);
}
