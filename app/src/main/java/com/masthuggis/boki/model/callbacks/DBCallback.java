package com.masthuggis.boki.model.callbacks;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface DBCallback {
    void onCallBack(List<Map<String, Object>> dataList);
}
