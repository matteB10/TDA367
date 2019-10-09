package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.StylingHelper;

import java.io.File;
import java.util.List;

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
        view.setTags(advertisement.getTags());

    }

    public boolean isPriceValid() {
        return this.validPrice;
    }

    public void titleChanged(String title) {
        String adID = advertisement.getUniqueID();
        if(title != null) {
            advertisement.setTitle(title.toString());
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
/*
    public void tagsChanged(String tag){
        String adID = advertisement.getUniqueID();
        List<String> tagList= advertisement.getTags();
        tagList.add(tag);
    }

 */

    public void removeAdBtnPressed(){
        String adID = advertisement.getUniqueID();
        DataModel.getInstance().removeExistingAdvert(adID);
    }

    public void saveAdBtnPressed(File currentImageFile){
        view.imageUpdate(currentImageFile);
    }

    public void imageUpdate(File imageFile){
        String adID = advertisement.getUniqueID();
        DataModel.getInstance().updateImage(imageFile,adID);
    }

    public void userDefTagsChanged(String tag) {
       String adID = advertisement.getUniqueID();
        if(isTagSelected(tag)) {
            view.displayUserTagButton(tag);
        }else{
            view.removeUserTagButton(tag);
        }
        advertisement.tagsChanged(tag);
    }


    public void conditionChanged(int condition) {
        advertisement.setCondition(condition);
        view.styleConditionButtonPressed(condition);
    }

    public interface View   {
        void setTitle(String name);

        void setPrice(long price);

        void setImageUrl(String url);

        void setDescription(String description);

        void styleConditionButtonPressed(int condition);

        void imageUpdate(File imageFile);

        void setTags(List<String> tags);
        void setTagStyling(String tag, boolean isPressed);
        void displayUserTagButton(String tag);
        void removeUserTagButton(String tag);
        File getCurrentImageFile();

    }
    //----------------------------------
    public void preDefTagsChanged(String tag) {
        view.setTagStyling(tag, isTagSelected(tag));
        advertisement.tagsChanged(tag);
    }

    private boolean isTagSelected(String tag) {
        return advertisement.isNewTag(tag);
    }

    public int getTagDrawable(boolean isPressed) {
        return StylingHelper.getTagDrawable(isPressed);
    }


}
