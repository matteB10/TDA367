package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.backend.iRepository;
import com.masthuggis.boki.model.Advertisement;

public class DetailsPresenter {
    private View view;
    private iRepository modelRepository;
    private Advertisement advertisement;

    public DetailsPresenter(View view, String advertID) {
        this.view = view;
        this.modelRepository = Repository.getInstance();
        modelRepository.getAllAds();
        this.advertisement = modelRepository.getAdFromId(advertID);

        setupView();
    }

    private void setupView() {
        view.setName(advertisement.getName());
        view.setPrice(advertisement.getPrice());
        if (advertisement.getImgURL() != null) {
            view.setImageUrl(advertisement.getImgURL().toString());
        }
    }

    public interface View {
        void setName(String name);
        void setPrice(int price);
        void setImageUrl(String url);
    }
}
