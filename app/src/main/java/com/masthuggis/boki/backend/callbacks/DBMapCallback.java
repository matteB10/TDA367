package com.masthuggis.boki.backend.callbacks;

import java.util.Map;

@FunctionalInterface
public interface DBMapCallback {
        void onCallBack(Map<String, Object> dataMap);
    }
