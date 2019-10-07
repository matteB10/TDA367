package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;

public class EditAdPresenter {

    private Advertisement advertisement;
    private View view;


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



    public void titleChanged(String title) {
        String adID = advertisement.getUniqueID();
        advertisement.setTitle(title);
        DataModel.getInstance().updateTitle(adID, title);
    }

    public void priceChange(String price){
        String adID = advertisement.getUniqueID();
        advertisement.setPrice(Integer.parseInt(price));
        DataModel.getInstance().updatePrice(adID, price);
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




    public interface View {
        void setTitle(String name);

        void setPrice(long price);

        void setImageUrl(String url);

        void setDescription(String description);
    }
}
