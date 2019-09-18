package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advert;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class CreateAdPresenter{

    private View view;
    private String title;
    private String description;
    private int price;
    private String imageUri;
    private List<String> tags;
    private Advert.Condition condition;



    public CreateAdPresenter(View view){
        this.view = view;
    }

    public interface View{

        //TODO: create methods for future same page error messages in view
        void deleteTag(String tag);
    }

    //arbitrary length, can be changed
    public void titleChanged(String title){
        if (title.length() > 6) {
            this.title = title;
        }
    }
    public void priceChanged(String price){
        if(FormHelper.getInstance().isValidPrice(price)){
            this.price = Integer.parseInt(price);
        }
    }
    //cannot check for valid input, all input is valid
    public void descriptionChanged(String description){
        this.description = description;
    }
    public void imageURIChanged(String imageURI){
        this.imageUri = convertURIStringToURLString(imageURI);
    }

    public void tagsChanged(String tag){
        if(isNewTag(tag)){
            tags.add(tag);
        }
    }
    public void conditionChanged(Advert.Condition condition){
        this.condition = condition;

    }
    public void createAdvert(){
        Repository.getInstance().createAdvert(title,description,price,condition,tags,imageUri);
    }


    private boolean isNewTag(String tag){
        for(String s : tags){
            if(s.equals(tag)){
                return false;
            }
        }
        return true;
    }
    //TODO: Maybe put a standard URL to some image in the catch block
    private String convertURIStringToURLString(String URI){
        String URLString;
        try {
            URL url = new URL(URI);
            URLString = url.toString();
        }catch (MalformedURLException e){
            URLString = "";
        }
        return URLString;
    }


}
