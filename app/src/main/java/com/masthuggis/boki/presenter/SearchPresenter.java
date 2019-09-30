package com.masthuggis.boki.presenter;


import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advertisement;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter {
    private List<Advertisement> adverts;
    private final View view;

    public SearchPresenter(View view) {
        this.view = view;
        this.view.showLoadingScreen();
        getData();
    }

    private void getData() {
        Repository.getInstance().getAllAds(advertisements -> {
            if (advertisements != null)
                updateLocalData(advertisements);
        });
    }

    private void updateLocalData(List<Advertisement> adverts) {
        this.adverts = new ArrayList<>(adverts);
        this.view.hideLoadingScreen();
    }


    public interface View {
        void showLoadingScreen();

        //possibly quite useful depending on how long the search queries take
        void hideLoadingScreen();
    }

}
