package com.masthuggis.boki.presenter;

import android.provider.ContactsContract;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;

import java.io.File;

public class EditAdPresenter {

    private Advertisement advertisement;
    private View view;
    private static boolean validPrice;


    public EditAdPresenter(View view, String advertID){
        this.view = view;
        this.advertisement = DataModel.getInstance().getAdFromAdID(advertID);
        setUpView();
    }

    public String getID(){
        return advertisement.getUniqueID();
    }

    private void setUpView(){
        view.setTitle(advertisement.getTitle());
        view.setPrice(advertisement.getPrice());
        view.setDescription(advertisement.getDescription());
        view.setImageUrl(advertisement.getImageUrl());
    }

    public boolean isPriceValid() {
        return this.validPrice;
    }

    public void titleChanged(String title) {
        String adID = advertisement.getUniqueID();
        if(title != null) {
            advertisement.setTitle(title);
            DataModel.getInstance().updateTitle(adID, title);
        }
    }

    public void priceChange(String price){
        String adID = advertisement.getUniqueID();
        if(isPriceValid()) {
            advertisement.setPrice(Integer.parseInt(price));
            DataModel.getInstance().updatePrice(adID, price);
        }
    }

    public void descriptionChanged(String description){
        String adID = advertisement.getUniqueID();
        advertisement.setDescription(description);
        DataModel.getInstance().updateDescription(adID, description);
    }


    public void removeAdBtnPressed(){
        String adID = advertisement.getUniqueID();
        DataModel.getInstance().removeExistingAdvert(adID);
    }

    public void saveAdBtnPressed() {
        DataModel.getInstance().saveAdvert(new File(advertisement.getImageUrl()),advertisement); //This is never run????
    }

    public void conditionChanged(int condition) {
        advertisement.setCondition(condition);
        view.styleConditionButtonPressed(condition);

    }

    public interface View {
        void setTitle(String name);

        void setPrice(long price);

        void setImageUrl(String url);

        void setDescription(String description);

        void styleConditionButtonPressed(int condition);
    }
}
