package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advertisement;

import java.util.Iterator;
import java.util.List;

//TODO OBSERVER ON HOLD, EVENTUELL SENARE IMPLEMENTATION
public interface RepositoryObserver {
    void advertsInMarketUpdate(List<Advertisement> advertsInMarket);
}
