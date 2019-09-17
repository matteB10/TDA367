package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.iRepository;
import com.masthuggis.boki.model.Advertisement;

public class DetailsPresenter {
    private View view;
    private iRepository modelRepository;
    private Advertisement advertisement;

    public DetailsPresenter(View view, iRepository modelRepository) {
        this.view = view;
        this.modelRepository = modelRepository;
        //this.advertisement = modelRepository.getAdFromId()
    }

    public interface View {

    }
}
