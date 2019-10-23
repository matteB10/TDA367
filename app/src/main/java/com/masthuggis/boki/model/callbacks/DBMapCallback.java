package com.masthuggis.boki.model.callbacks;

import java.util.Map;

@FunctionalInterface
public interface DBMapCallback {
        void onCallBack(Map<String, Object> dataMap);
    }
