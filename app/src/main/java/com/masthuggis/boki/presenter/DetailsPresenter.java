package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advertisement;

public class DetailsPresenter {
    private View view;
    private Advertisement advertisement;

    public DetailsPresenter(View view, String advertID) {
        this.view = view;
        this.advertisement = Repository.getInstance().getAdFromId(advertID);

        setupView();
    }

    private void setupView() {
        view.setName(advertisement.getTitle());
        view.setPrice(advertisement.getPrice());
        view.setDescription(advertisement.getDescription());
      /*  if (advertisement.getImgURLs().next() != null) {
            view.setImageUrl(advertisement.getImgURLs().next());
        }*/
    }

    public interface View {
        void setName(String name);
        void setPrice(int price);
        void setImageUrl(String url);
        void setDescription(String description);
    }
}
