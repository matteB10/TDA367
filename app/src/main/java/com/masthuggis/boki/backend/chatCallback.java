package com.masthuggis.boki.backend;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface chatCallback {
    void onCallback(List<Map<String,Object>> chatMap);
}
