package com.masthuggis.boki.backend;

import com.masthuggis.boki.model.Advertisement;

import java.util.Iterator;

//TODO OBSERVER ON HOLD, EVENTUELL SENARE IMPLEMENTATION
public interface RepositoryObserver {
    void userAdvertsForSaleUpdate(Iterator<Advertisement> advertsForSale);
    void allAdvertsInMarketUpdate(Iterator<Advertisement> advertsInMarket);
}
