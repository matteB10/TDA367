package com.masthuggis.boki.presenter;

import java.net.URI;

public class CreateAdPresenter{

    private View view;

    interface View{
        void titleChanged(String title);
        void authorChanged(String author);
        void priceChanged(String price);
        void descriptionChanged(String description);
        void imageURIChanged(URI imageURI);
    }

}
