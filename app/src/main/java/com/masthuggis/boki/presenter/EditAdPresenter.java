package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;

public class EditAdPresenter {

    private String title;
    private Advert.Condition condition;
    private String description;
    private Long price;

    private Advertisement advertisement;

    public EditAdPresenter(View view, String advertID){
        this.advertisement = DataModel.getInstance().getAdFromAdID(advertID);
    }

    public void removeAdBtnPressed(){
        String adID = advertisement.getUniqueID();
        DataModel.getInstance().removeExistingAdvert(adID);
    }

    public void editTitleBtnPressed() {
        String adID = advertisement.getUniqueID();
        DataModel.getInstance().updateTitle(adID);
    }



    public interface View {
       void backToHomeview();
       void getFieldForTitle();
    }
}
