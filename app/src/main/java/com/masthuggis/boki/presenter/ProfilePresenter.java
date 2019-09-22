package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;

import java.util.List;

public class ProfilePresenter {
    private View view;
    private List<Advertisement> userItemsOnSale;
    private Repository repository;

    public ProfilePresenter(View view) {
        this.view = view;
        this.repository = Repository.getInstance();
        // TODO: for now using temp data, later use real advertisment of the actual user
        this.userItemsOnSale = repository.getTemporaryListOfAllAds();
    }

    public interface View {
        void setIsUserLoggedIn(boolean isUserLoggedIn);
        void updateItemsOnSale();
    }
}
