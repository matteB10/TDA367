package com.masthuggis.boki.view;

import java.util.List;

interface OnFragmentCommunicationListener {

    void onPriceChanged(int price);
    void onFilterTagsChanged(List<String> tags);
}
