package com.masthuggis.boki.presenter;

import java.net.URI;

public class CreateAdPresenter{

    private View view;


    public CreateAdPresenter(View view){
        this.view = view;
    }

    void titleChanged(String title){}
    void authorChanged(String author){}
    void priceChanged(String price){}
    void descriptionChanged(String description){}
    void imageURIChanged(URI imageURI){

    }
    void ISBNChanged(String ISBN){

    }
    void tagsChanged(String tag){

    }

    public interface View{

        void deleteTag(String tag);
    }

}
