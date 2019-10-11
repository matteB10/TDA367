package com.masthuggis.boki.backend.callbacks;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface DBCallback {
    void onCallBack(List<Map<String, Object>> dataList);
}
